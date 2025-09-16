package threeoone.bigproject.controller.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.requestbodies.LendingDetail;
import threeoone.bigproject.services.persistence.LendingPersistenceService;
import threeoone.bigproject.util.Alerts;

/**
 * The {@code ActionOnLendingController} class handles actions related to lending operations.
 * It is responsible for saving new lending details through the lending persistence service.
 *
 * @author HUY1902
 */
@Component
@RequiredArgsConstructor
public class ActionOnLendingController {
  private final Logger logger = LoggerFactory.getLogger(ActionOnLendingController.class);

  private final LendingPersistenceService lendingPersistenceService;

  /**
   * Registers the request receiver for saving new lending details.
   *
   * @param saveNewLending the request sender for saving new lending details
   */
  @Autowired
  private void registerRequestReceiver(RequestSender<LendingDetail> saveNewLending,
                                       RequestSender<Integer> deleteLending) {
    saveNewLending.registerReceiver(this::saveNewLending);
    deleteLending.registerReceiver(this::deleteLendingById);
  }

  /**
   * Saves the new lending detail by delegating the operation to the lending persistence service.
   *
   * @param newLending the new lending detail to be saved
   */
  public void saveNewLending(LendingDetail newLending) {
    Alerts.showErrorWithLogger(() -> {
      lendingPersistenceService.lend(newLending.member().getId(), newLending.document().getId());
    }, logger);
  }

  /**
   * Delete te lending detail by calling to service
   *
   * @param ID lending detail id need to be deleted
   */
  public void deleteLendingById(int ID) {
    Alerts.showErrorWithLogger(() -> {
      lendingPersistenceService.delete(ID);
    }, logger);
  }
}
