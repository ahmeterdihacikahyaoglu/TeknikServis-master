package com.garanti.TeknikServis.service;

import com.garanti.TeknikServis.excepton.EntityNoContentException;
import com.garanti.TeknikServis.excepton.UnexpectedException;
import com.garanti.TeknikServis.model.Sale;
import com.garanti.TeknikServis.model.SaleDto;
import com.garanti.TeknikServis.repo.SaleRepository;
import com.garanti.TeknikServis.repo.UserRepo;
import com.garanti.TeknikServis.security.TokenParser;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class SaleService {

    private SaleRepository repository;
    private UserRepo userRepo;
    private MessageSource messageSource;

    public List< Sale > getAll() {
        return repository.getAll();
    }

    public boolean deleteById(int id) {
        return repository.deleteById(id);
    }

    public boolean save ( Sale sale)
    {
        try
        {
            return repository.save(sale);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    // User Servis Kısmı:
    public List <SaleDto> getListofSales(Locale locale){
        List<SaleDto> list = repository.getListofSales();
        if(!list.isEmpty())
            return list;
        throw new EntityNoContentException(messageSource.getMessage("sale.no.content", null, locale));
    }
    public List <SaleDto> getListofSalesByProduct(String productType, Locale locale){
        List<SaleDto> list = repository.getListofSalesByProduct(productType);
        if(!list.isEmpty())
            return list;
        throw new EntityNoContentException(messageSource.getMessage("sale.no.content", null, locale));
    }

    public List <SaleDto> getListofSalesByProductId(Integer productID, Locale locale){
        List<SaleDto> list = repository.getListofSalesByProductId(productID);
        if(!list.isEmpty() && productID != null)
            return list;
        throw new EntityNoContentException(messageSource.getMessage("sale.no.content", null, locale));
    }
    @Transactional
    public String buyTheProductInAd(Integer id, String creditcard, HttpHeaders headers, Locale locale){
        if(id != null && creditcard != null && creditcard.length() == 16){
            String username = TokenParser.jwt(headers.get("Authorization").get(0).substring(7));
            int userid = userRepo.getUserId(username);
            if(repository.buyTheProductInAd(id)){
                if(repository.insertSaleToSaleLog(id,userid,creditcard))
                    return messageSource.getMessage("purchase.success", null, locale);
                throw new UnexpectedException(messageSource.getMessage("purchase.unexpected.fail", null, locale));
            }
            throw new IllegalArgumentException(messageSource.getMessage("purchase.no.product", null, locale));

        }
        throw new IllegalArgumentException(messageSource.getMessage("purchase.fail", null, locale));


    }
}
