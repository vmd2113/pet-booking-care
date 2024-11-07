package com.duongw.universalpetcare.event.listener;

import com.duongw.universalpetcare.email.EmailService;
import com.duongw.universalpetcare.event.*;
import com.duongw.universalpetcare.model.Appointment;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.service.IVerificationTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class NotificationEventListener implements ApplicationListener<ApplicationEvent> {

    private final EmailService emailService;
    private final IVerificationTokenService verificationTokenService;

    @Value("${frontend.base.url}")
    private String frontendUrl;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Object source = event.getSource();

        switch (event.getClass().getSimpleName()) {

            case "RegistrationCompleteEvent":
                if (source instanceof User) {
                    handleSendRegistrationVerificationEmail((RegistrationCompleteEvent) event);
                }
                break;

            case "AppointmentBookingEvent":
                if (source instanceof Appointment) {
                    try {
                        handleAppointmentBookedNotification((AppointmentBookingEvent) event);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "AppointmentApprovedEvent":
                if (source instanceof Appointment) {
                    try {
                        handleAppointmentApprovedNotification((AppointmentApprovedEvent) event);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "AppointmentDeclinedEvent":
                if (source instanceof Appointment) {
                    try {
                        handleAppointmentDeclinedNotification((AppointmentDeclinedEvent) event);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "PasswordResetEvent":
                PasswordResetEvent passwordResetEvent = (PasswordResetEvent) event;
                handlePasswordResetRequest(passwordResetEvent);
                break;


            default:
                break;
        }

    }


    /*=================== Start user registration email verification ============================*/
    private void handleSendRegistrationVerificationEmail(RegistrationCompleteEvent event) {
        User user = event.getUser();
        // Generate a token for the user
        String vToken = UUID.randomUUID().toString();
        // Save the token for the user
        verificationTokenService.saveVerificationTokenForUser(vToken, user);
        // Build the verification url
        String verificationUrl = frontendUrl + "/email-verification?token=" + vToken;
        try {
            sendRegistrationVerificationEmail(user, verificationUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRegistrationVerificationEmail(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Verify Your Email";
        String senderName = "Universal Pet Care";
        String mailContent = "<p> Hi, " + user.getFirstName() + ", </p>" +
                "<p>Thank you for registering with us," +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify your email</a>" +
                "<p> Thank you <br> Universal Pet Care Email Verification Service";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*=================== End user registration email verification ============================*/



    /*======================== Start New Appointment booked notifications ===================================================*/

    private void handleAppointmentBookedNotification(AppointmentBookingEvent event) throws MessagingException, UnsupportedEncodingException {
        Appointment appointment = event.getAppointment();
        User vet = appointment.getVeterinarian();
        sendAppointmentBookedNotification(vet, frontendUrl);
    }

    private void sendAppointmentBookedNotification(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "New Appointment Notification";
        String senderName = "Universal Pet Care";
        String mailContent = "<p> Hi, " + user.getFirstName() + ", </p>" +
                "<p>You have a new appointment schedule:</p>" +
                "<a href=\"" + url + "\">Please, check the clinic portal to view appointment details.</a> <br/>" +
                "<p> Best Regards.<br> Universal Pet Care Service";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End New Appointment Booked notifications ===================================================*/


    /*======================== Start Approve Appointment notifications ===================================================*/

    private void handleAppointmentApprovedNotification(AppointmentApprovedEvent event) throws MessagingException, UnsupportedEncodingException {
        Appointment appointment = event.getAppointment();
        User patient = appointment.getPatient();
        sendAppointmentApprovedNotification(patient, frontendUrl);
    }

    private void sendAppointmentApprovedNotification(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Appointment Approved";
        String senderName = "Universal Pet Care Notification Service";
        String mailContent = "<p> Hi, " + user.getFirstName() + ", </p>" +
                "<p>Your appointment has been approved:</p>" +
                "<a href=\"" + url + "\">Please, check the clinic portal to view appointment details " +
                "and veterinarian information.</a> <br/>" +
                "<p> Best Regards.<br> Universal Pet Care";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End Approve Appointment notifications ===================================================*/


    /*======================== Start Decline Appointment notifications ===================================================*/

    private void handleAppointmentDeclinedNotification(AppointmentDeclinedEvent event) throws MessagingException, UnsupportedEncodingException {
        Appointment appointment = event.getAppointment();
        User patient = appointment.getPatient();
        sendAppointmentDeclinedNotification(patient, frontendUrl);
    }

    private void sendAppointmentDeclinedNotification(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Appointment Not Approved";
        String senderName = "Universal Pet Care Notification Service";
        String mailContent = "<p> Hi, " + user.getFirstName() + ", </p>" +
                "<p>We are sorry, your appointment was not approved at this time,<br/> " +
                "Please, kindly make a reschedule for another date. Thanks</p>" +
                "<a href=\"" + url + "\">Please, check the clinic portal to view appointment details.</a> <br/>" +
                "<p> Best Regards.<br> Universal Pet Care";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End Decline Appointment notifications ===================================================*/



    /*======================== Start password reset related notifications ===================================================*/

    private void handlePasswordResetRequest(PasswordResetEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.saveVerificationTokenForUser(token, user);
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        try {
            sendPasswordResetEmail(user, resetUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    private void sendPasswordResetEmail(User user, String resetUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Request";
        String senderName = "Universal Pet Care";
        String mailContent = "<p>Hi, " + user.getFirstName() + ",</p>" +
                "<p>You have requested to reset your password. Please click the link below to proceed:</p>" +
                "<a href=\"" + resetUrl + "\">Reset Password</a><br/>" +
                "<p>If you did not request this, please ignore this email.</p>" +
                "<p>Best Regards.<br> Universal Pet Care</p>";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End password reset related notifications ===================================================*/


}
