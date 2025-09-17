package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO с основной информацией о поставщике")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponseDto {

    @Schema(description = "Идентификатор поставщика ")
    private UUID id;

    @Schema(description = "Наименование поставщика ")
    private String name;

    @Schema(description = "Электронная почта поставщика ")
    private String email;

    @Schema(description = "Телефонный номер поставщика ")
    private String phone;

    @Schema(description = "Адрес поставщика ")
    private String address;

    @Schema(description = "Веб сайт поставщика ")
    private String website;

    @Schema(description = "Дата последнего обновления данных о поставщике")
    private LocalDateTime updatedAt;

}
