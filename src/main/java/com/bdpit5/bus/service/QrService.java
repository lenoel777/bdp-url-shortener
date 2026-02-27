package com.bdpit5.bus.service;

import com.bdpit5.bus.dto.*;
import com.bdpit5.bus.entity.QrCodeEntity;
import com.bdpit5.bus.repository.QrRepository;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QrService {

    public QrResponse generate(QrRequest request) {

        int size = request.getSize() != null ? request.getSize() : 250;

        String base64 = generateQrBase64(request.getOriginalUrl(), size);

        String fullBase64 = "data:image/png;base64," + base64;

        QrCodeEntity entity = QrCodeEntity.builder()
                .originalUrl(request.getOriginalUrl())
                .qrCodeBase64(fullBase64)
                .createdAt(LocalDateTime.now())
                .build();

        qrRepository.save(entity);

        QrResponse.OutputSchema output =
                QrResponse.OutputSchema.builder()
                        .qrCodeBase64("data:image/png;base64," + base64)
                        .build();

        return buildResponse("00", "Berhasil", "Success", List.of(output));
    }

    private String generateQrBase64(String text, int size) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }

        if (size < 100) {
            size = 250;
        }

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix =
                    qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return Base64.getEncoder()
                    .encodeToString(outputStream.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    private final QrRepository qrRepository;

    public QrResponse inquiryAll() {

        List<QrCodeEntity> qrList = qrRepository.findAll();

        List<QrResponse.OutputSchema> outputs =
                qrList.stream()
                        .map(qr -> QrResponse.OutputSchema.builder()
                                .id(qr.getId())
                                .originalUrl(qr.getOriginalUrl())
                                .qrCodeBase64(qr.getQrCodeBase64())
                                .createdAt(qr.getCreatedAt().toString())
                                .build())
                        .collect(Collectors.toList());

        return buildResponse("00", "Berhasil", "Success", outputs);
    }

    public QrResponse deleteById(Long id) {
        try {
            QrCodeEntity entity = qrRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("QR Code ID not found: " + id));

            qrRepository.delete(entity);

            return buildResponse(
                    "200",
                    "Data QR berhasil dihapus",
                    "QR data deleted successfully",
                    List.of()
            );

        } catch (IllegalArgumentException e) {
            return buildResponse(
                    "404",
                    e.getMessage(),
                    "Not Found",
                    List.of()
            );
        } catch (Exception e) {
            return buildResponse(
                    "500",
                    "Gagal menghapus data QR",
                    "Internal server error",
                    List.of()
            );
        }
    }

    public QrResponse deleteAllQr() {
        try {
            qrRepository.deleteAll();

            return buildResponse(
                    "200",
                    "Semua data QR berhasil dihapus",
                    "All QR data deleted successfully",
                    List.of()
            );

        } catch (Exception e) {
            return buildResponse(
                    "500",
                    "Gagal menghapus semua data QR",
                    "Internal server error",
                    List.of()
            );
        }
    }

    private QrResponse buildResponse(
            String code,
            String idMessage,
            String enMessage,
            List<QrResponse.OutputSchema> outputs
    ) {

        ErrorMessage errorMessage =
                new ErrorMessage(idMessage, enMessage);

        ErrorSchema errorSchema =
                new ErrorSchema(errorMessage, code);

        return QrResponse.builder()
                .errorSchema(errorSchema)
                .outputSchemas(outputs)
                .build();
    }
}