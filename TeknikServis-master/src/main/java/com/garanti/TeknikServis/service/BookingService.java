package com.garanti.TeknikServis.service;
import com.garanti.TeknikServis.excepton.EntityNoContentException;
import com.garanti.TeknikServis.model.Booking;
import com.garanti.TeknikServis.model.BookingDTO;
import com.garanti.TeknikServis.model.Service;
import com.garanti.TeknikServis.repo.BookingRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class BookingService {
    @NonNull
    private BookingRepo appointmentRepo;
    @NonNull
    private MessageSource messageSource;


    public boolean deleteById(int id)
    {
        return appointmentRepo.deleteById(id);
    }

    public BookingDTO getById(int id)
    {
        return appointmentRepo.getById(id);
    }

    public boolean save (Booking booking)
    {
        try
        {
            Service service = appointmentRepo.service_hour_control(booking);
            Date date=createBookingDate(service.getDURATION());
            return appointmentRepo.save(booking,date);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return false;
        }
    }

private Date createBookingDate(Integer serviceDuration)
{
    LocalDate bookingDate = LocalDate.now();
    Date sqlDate = Date.valueOf(bookingDate);
    bookingDate = bookingDate.plusDays(-1L);
    int duration = 0;
    do
    {
        duration = serviceDuration;
        bookingDate = bookingDate.plusDays(1L);
        sqlDate = Date.valueOf(bookingDate);
        //List<Booking> bookings = appointmentRepo.dateControl(sqlDate);
        List<Service> services=appointmentRepo.getDurationList(sqlDate);
        for (Service services1 : services)
        {
            duration += services1.getDURATION();
        }
    }
    while (duration > 10);
    return sqlDate;
}

    //http://localhost:9090/booking/getAppointmentsDatesInOrder?sortType=asc
    public List<Booking> getAppointmentsDatesInOrder(String sortType, Locale locale) {
        if (sortType.equalsIgnoreCase("DESC") || sortType.equalsIgnoreCase("ASC")) {
            List<Booking> bookings = appointmentRepo.getAppointmentDatesInOrder(sortType);
            if (!bookings.isEmpty())
                return bookings;
            throw new EntityNoContentException(messageSource.getMessage("booking.no.content", null, locale));
        }
        throw new IllegalArgumentException(messageSource.getMessage("booking.wrong.sort.parameter", null, locale));
    }

    //http://localhost:9090/booking/getAllAppointmentLikeUsername?username=A
    public List<Booking> getAllAppointmentLikeUsername(String username, Locale locale) {
        if (username != null) {
            List<Booking> bookings = appointmentRepo.getAllAppointmentLikeUsername(username);
            if (!bookings.isEmpty())
                return bookings;
        }
        throw new EntityNoContentException(messageSource.getMessage("booking.no.content", null, locale));
    }



    public Booking appointmentIsComplete(int id,boolean is_done, Locale locale) {
        if (appointmentRepo.appointmentIsComplete(id,is_done)) {
            return appointmentRepo.getBookingById(id);
            }
        throw new EntityNoContentException(messageSource.getMessage("booking.no.content", null, locale));
    }

}
