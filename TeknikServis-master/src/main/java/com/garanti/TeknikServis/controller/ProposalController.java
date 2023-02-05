package com.garanti.TeknikServis.controller;

import com.garanti.TeknikServis.model.Proposal;
import com.garanti.TeknikServis.response.RestResponse;
import com.garanti.TeknikServis.service.ProposalService;
import com.garanti.TeknikServis.validation.ProposalValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("proposal")
@AllArgsConstructor
@Tag (name = "Proposal Service", description = "This class enables to proposal operations.")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class ProposalController {
    private ProposalService proposalService;
    private ProposalValidator proposalValidator;


    @PostMapping(path = "save")
    @Secured(value = "ROLE_USER")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "2.El ürün satışı için teklif oluşturma endpointi",
            description = "İstekte bulunan kullanıcı eğer gönderdiği format doğruysa veritabanına 2.el ürün kaydı gerçekleşecektir.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Başarılı bir şekilde veri kaydedildiğinde alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "İstekte bulunmaya yetkiniz yoksa alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    ),
                    @ApiResponse(responseCode = "418",
                            description = "Save işlemi yaparken herhangi bir hata ile karşılaşırsanız alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }

                    )
            }
    )
    public ResponseEntity save(@Validated BindingResult result, @RequestBody Proposal proposal, @RequestHeader(value = "Authorization") HttpHeaders headers, @RequestHeader(name = "Accept-Language", required = false) Locale locale){
        //http://localhost:9090/proposal/save

        proposalValidator.validate(proposal, result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(RestResponse.of(proposalService.save(proposal, headers, locale)));
    }
    @DeleteMapping(path = "deleteByProposalId")
    @Secured(value = "ROLE_USER")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "İstekte bulunan kullanıcının sistemdeki teklifini siler.",
            description = "İstekte bulunan kullanıcının sistemde mevcut bir teklifi varsa silme işlemini gerçekleştirir.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Başarılı bir şekilde veri kaydedildiğinde alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    ),
                    @ApiResponse(responseCode = "226",
                            description = "İstekte bulunduğunuz parametre yapısında hata oluştuğunda alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "İstekte bulunmaya yetkiniz yoksa alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    ),
                    @ApiResponse(responseCode = "418",
                            description = "Save işlemi yaparken herhangi bir hata ile karşılaşırsanız alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }

                    )
            }
    )
    public ResponseEntity deleteByProposalId(@RequestHeader(name = "Accept-Language", required = false)@RequestParam(value ="proposalID") Integer proposalID,
                                             @RequestHeader(value = "Authorization") HttpHeaders headers, Locale locale){
        //http://localhost:9090/proposal/deleteByProposalId?proposalID=
        return ResponseEntity.ok(RestResponse.of(proposalService.deleteByProposalId(proposalID,headers, locale)));
    }
    @GetMapping(path = "getByUserOffers")
    @Secured(value = "ROLE_USER")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "İstekte bulunan kullanıcının tekliflerini listeleyen endpoint",
            description = "JWT token ile istekte bulunan kişinin kullanıcı id'si parse edilir ve veritabanına istekte bulunulur. Eğer kayıtlı teklifi varsa onay durumu gözetmeksizin tüm teklifleri listelenir.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Başarılı bir şekilde veri getirildiğinde alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "İstekte bulunmaya yetkiniz yoksa alacağınız response status kodu.",
                            content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                    }
                    ),
                    @ApiResponse(responseCode = "418",
                            description = "İstekde bulunduğunuz tablo boş veya null bir şekilde geri dönüş sağlarsa alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }

                    )
            }
    )
    public ResponseEntity getByUserOffers(@RequestHeader(value = "Authorization") HttpHeaders headers,@RequestHeader(name = "Accept-Language", required = false) Locale locale){
        //http://localhost:9090/proposal/getByUserOffers
        return ResponseEntity.ok(RestResponse.of(proposalService.getByUserOffers(headers, locale)));
    }
    @GetMapping(path = "getByApprovedOffers")
    @Secured(value = "ROLE_USER")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "İstekte bulunan kullanıcının onaylanmış tekliflerini listeleyen endpoint",
            description = "JWT token ile istekte bulunan kişinin kullanıcı id'si parse edilir ve veritabanına istekte bulunulur. Eğer kayıtlı teklifi varsa onaylanmış olarak işaretlenen tekliflerini listeler.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Başarılı bir şekilde veri getirildiğinde alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "İstekte bulunmaya yetkiniz yoksa alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }
                    ),
                    @ApiResponse(responseCode = "418",
                            description = "İstekde bulunduğunuz tablo boş veya null bir şekilde geri dönüş sağlarsa alacağınız response status kodu.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = RestResponse.class)))
                            }

                    )
            }
    )
    public ResponseEntity getByApprovedOffers(@RequestHeader(value = "Authorization") HttpHeaders headers,@RequestHeader(name = "Accept-Language", required = false) Locale locale){
        //http://localhost:9090/proposal/getByApprovedOffers
        return ResponseEntity.ok(RestResponse.of(proposalService.getApprovedOffers(headers, locale)));
    }
    @Operation (summary = "This method is to fetch the all proposals from database.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("getall")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<?> getAll(@RequestHeader(name = "Accept-Language", required = false) Locale locale){
        //localhost:9090/proposal/getall
        return ResponseEntity.ok(RestResponse.of(proposalService.getAll(locale)));
    }

    @Operation (summary = "This method is to fetch the proposals that saved with ID in database.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("getById")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<?> getById(@Parameter (name = "PROPOSAL ID", required = true) @RequestParam("id") long id){
        //localhost:9090/proposal/getById?id=1
        return ResponseEntity.ok(proposalService.getById(id));
    }
    @Operation (summary = "This method is to update the proposals.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("updateById")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<?> updateById(@Parameter(name = "Proposal ID", required = true)   @RequestParam("id") long id,
                                        @Parameter(name = "Is Approved?", required = true) @RequestParam("approval") boolean approval){
        //localhost:9090/proposal/updateById?id=1&approval=true
        return ResponseEntity.ok(proposalService.updateById(id,approval));
    }
}
