package threeoone.bigproject.controller.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.viewcontrollers.ForgetPasswordController;
import threeoone.bigproject.services.forgotpassword.PasswordResetLinkSenderService;
import threeoone.bigproject.util.Alerts;

@Component
@RequiredArgsConstructor
public class ResetLinkController {
    private final Logger logger = LoggerFactory.getLogger(ResetLinkController.class);

    private final PasswordResetLinkSenderService passwordResetLinkSenderService;
    private final ForgetPasswordController forgetPasswordController;

    @Autowired
    private void registerRequestSenders(RequestSender<String> sendResetLinkRequestSender) {
        sendResetLinkRequestSender.registerReceiver(this::sendTo);
    }

    private void sendTo(String username) {
        try {
            if (!passwordResetLinkSenderService.sendResetLink(username)) {
                Alerts.showAlertWarning("Error!", "No user with that username exists");
            }
        }
        catch (RuntimeException e) {
            Alerts.showAlertWarning("Error", "Unexpected error occurred. Please try again later");
            logger.warn(e.getMessage());
        }
    }

}
