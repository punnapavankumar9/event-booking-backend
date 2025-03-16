package com.punna.eventbooking.notification.service;

import com.punna.commons.eventing.events.kafka.order.OrderCancelledEvent;
import com.punna.commons.eventing.events.kafka.order.OrderFailedEvent;
import com.punna.commons.eventing.events.kafka.order.OrderSuccessEvent;
import com.punna.eventbooking.notification.dto.EventInfo;
import com.punna.eventbooking.notification.dto.OrderEmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final SpringTemplateEngine templateEngine;
  private final JavaMailSender mailSender;

  @Async("emailTaskExecutor")
  public void sendOrderSuccessEmail(EventInfo eventInfo, OrderSuccessEvent orderSuccessEvent,
      String to) {
    String title = String.format("Booking Confirmed! for %s", eventInfo.event().getName());

    Context context = new Context();
    // Add order details
    context.setVariable("order", OrderEmailDto.builder()
        .id(orderSuccessEvent.id())
        .seats(orderSuccessEvent.seats())
        .amount(orderSuccessEvent.amount())
        .orderedAt(orderSuccessEvent.orderedAt())
        .build());

    // Add event info
    context.setVariable("eventInfo", eventInfo);

    sendEmail(to, title, "order-confirmation", context);
  }


  @SneakyThrows
  @Async("emailTaskExecutor")
  public void sendEmail(String to, String subject, String templateName, Context context) {
    try {
      log.info("Starting to process email to: {}", to);

      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message,
          true,
          StandardCharsets.UTF_8.name());

      // Set basic email properties
      helper.setTo(to);
      helper.setSubject(subject);

      String htmlContent = templateEngine.process(templateName, context);
      helper.setText(htmlContent, true);

      // Send email
      mailSender.send(message);
      log.info("Email successfully sent to: {}", to);

    } catch (MessagingException e) {
      log.error("Failed to send email to: {}", to, e);
      throw new RuntimeException("Failed to send email", e);
    }
  }

  public void sendOrderCancelledEmail(EventInfo eventInfo, OrderCancelledEvent event,
      String to) {
    String title = String.format("Booking Cancelled for %s", eventInfo.event().getName());

    Context context = new Context();
    // Add order details
    context.setVariable("order", OrderEmailDto.builder()
        .id(event.id())
        .seats(event.seats())
        .amount(event.amount())
        .orderedAt(event.orderedAt())
        .build());

    // Add event info
    context.setVariable("eventInfo", eventInfo);

    sendEmail(to, title, "order-cancelled", context);
  }

  public void sendOrderFailureEmail(EventInfo eventInfo, OrderFailedEvent event, String to) {
    String title = String.format("Booking Failed for %s", eventInfo.event().getName());

    Context context = new Context();
    // Add order details
    context.setVariable("order", OrderEmailDto.builder()
        .id(event.id())
        .seats(event.seats())
        .amount(event.amount())
        .orderedAt(event.orderedAt())
        .build());

    // Add event info
    context.setVariable("eventInfo", eventInfo);

    sendEmail(to, title, "order-failure", context);

  }
}
