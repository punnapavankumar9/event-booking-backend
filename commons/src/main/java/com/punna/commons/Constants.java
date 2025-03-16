package com.punna.commons;

public class Constants {

  public static final int FIXED_PARTITION_COUNT = 5;

  // order topics
  public static final String ORDER_CREATED_TOPIC = "order-created-topic";
  public static final String ORDER_SUCCESS_TOPIC = "order-success-topic";
  public static final String ORDER_FAILED_TOPIC = "order-failed-topic";
  public static final String ORDER_TIMEOUT_TOPIC = "order-timeout-topic";
  public static final String ORDER_CANCELLED_TOPIC = "order-cancelled-topic";
  public static final String ORDER_SUCCESS_VALIDATION_TOPIC = "order-success-validation-topic";

  public static final String PULSAR_10_MIN_DELAY_TOPIC = "pulsar-10-min-delay-topic";


  public static final String UNBLOCK_TICKET_TOPIC = "unblock-tickets-topic";
  public static final String PAYMENT_REFUND_TOPIC = "payment-refund-topic";
  public static final String PAYMENT_SUCCESS_TOPIC = "payment-success-topic";
  public static final String PAYMENT_FAILED_TOPIC = "payment-failed-topic";

  public static class OrderEvents {

    public static final String ORDER_SUCCESS = "ORDER_SUCCESS";
    public static final String ORDER_FAILED = "ORDER_FAILED";
    public static final String ORDER_CREATED = "ORDER_CREATED";
    public static final String ORDER_CANCELLED = "ORDER_CANCELLED";
    public static final String TICKETS_UNBLOCK = "TICKETS_UNBLOCK";
    public static final String ORDER_VALIDATION = "ORDER_VALIDATION";
  }

  public static class PaymentEvents {

    public static final String PAYMENT_SUCCESS = "PAYMENT_SUCCESS";
    public static final String PAYMENT_FAILED = "PAYMENT_FAILED";
    public static final String PAYMENT_REFUND = "PAYMENT_REFUND";
  }
}
