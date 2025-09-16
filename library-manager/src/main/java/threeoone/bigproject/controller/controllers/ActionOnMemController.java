package threeoone.bigproject.controller.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import javafx.collections.FXCollections;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.requestbodies.SwitchScene;
import threeoone.bigproject.controller.viewcontrollers.*;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.services.forgotpassword.PasswordResetLinkSenderService;
import threeoone.bigproject.services.persistence.MemberEditingService;
import threeoone.bigproject.services.retrieval.MemberRetrievalService;
import threeoone.bigproject.util.Alerts;

import java.util.Comparator;
import java.util.List;

/**
 * Interacting with view controller and service
 * Handle logic for Member manipulation
 * Get information from service and send action to view based on that information
 *
 * @author HUY1902
 */
@Component
@RequiredArgsConstructor
public class ActionOnMemController {
  private final Logger logger = LoggerFactory.getLogger(ActionOnMemController.class);

  private final MemberListController memberListController;
  private final MemberRetrievalService memberRetrievalService;
  private final MemberEditingService memberEditingService;
  private final AddNewMemController addNewMemController;
  private final EditMemController editMemController;
  private final MenuController menuController;
  private final MemberDetailsController memberDetailsController;

  /**
   * Registers request receivers for document handling.
   *
   * @param getAllMembersRequestSender the request sender for getting all member
   */
  @Autowired
  private void registerRequestReceiver(RequestSender<Member> getAllMembersRequestSender,
                                       RequestSender<Member> editMemberRequestSender,
                                       RequestSender<Member> commitChangeMemberRequestSender,
                                       RequestSender<Member> removeMemberRequestSender,
                                       RequestSender<Member> addMemberRequestSender,
                                       RequestSender<SwitchScene> getTopFiveMembersRequestSender,
                                       RequestSender<Member> memberDetailsRequestSender,
                                       RequestSender<ViewController> getOverdueMember) {
    getAllMembersRequestSender.registerReceiver(this::getListAllMembers);
    editMemberRequestSender.registerReceiver(this::editMember);
    commitChangeMemberRequestSender.registerReceiver(this::commitChangeMember);
    removeMemberRequestSender.registerReceiver(this::removeMember);
    addMemberRequestSender.registerReceiver(this::addMember);
    getTopFiveMembersRequestSender.registerReceiver(this::getTop5);
    memberDetailsRequestSender.registerReceiver(this::memberDetail);
    getOverdueMember.registerReceiver(this::getOverdueMember);
  }

  /**
   * Call {@link MemberRetrievalService} get list of member and set it for table in {@link MemberListController}
   *
   * @param viewController from {@link MemberListController}
   */
  private void getOverdueMember(ViewController viewController) {
    Alerts.showErrorWithLogger(()-> {
      memberListController.setOverdueMembers(FXCollections.observableList(memberRetrievalService.getOverdueMembers()));
    }, logger);
  }

  /**
   * return List of All Member in dataset
   *
   * @param member None
   */
  private void getListAllMembers(Member member) {
    Alerts.showErrorWithLogger(()->{
      memberListController.setAllMembers(FXCollections.observableList(memberRetrievalService.getAll()));
      memberListController.getAllMembers().sort(Comparator.comparing(Member::getId));
    }, logger);
  }

  private void getTop5(SwitchScene switchScene) {
    Alerts.showErrorWithLogger(()->{
      menuController.setUserList(FXCollections.observableArrayList(memberRetrievalService.findTop5Records()));
    }, logger);
  }

  /**
   * Edits the given member by setting it in the edit member controller.
   *
   * @param member the member to be edited
   */
  private void editMember(Member member) {
    Alerts.showErrorWithLogger(()->{
      editMemController.setChosenMember(member);
    }, logger);
  }

  /**
   * Adds a new member using the member editing service and sets it in the add new member controller.
   *
   * @param member the member to be added
   */
  private void addMember(Member member) {
    Alerts.showErrorWithLogger(()->{
      addNewMemController.setMember(memberEditingService.add(member));
    }, logger);
  }

  /**
   * Commits changes to the given member by updating it using the member editing service.
   *
   * @param member the member whose changes are to be committed
   */
  private void commitChangeMember(Member member) {
    Alerts.showErrorWithLogger(()->{
      memberEditingService.update(member);
    }, logger);
  }

  /**
   * Removes the given member by deleting it using the member editing service.
   *
   * @param member the member to be removed
   */
  private void removeMember(Member member) {
    Alerts.showErrorWithLogger(()->{
      memberEditingService.delete(member.getId());
    }, logger);
  }

  private void memberDetail(Member member) {
    Alerts.showErrorWithLogger(()->{
      Member temp = memberRetrievalService.findWithLendingDetails(member.getId());
      memberDetailsController.setMember(temp);
    }, logger);
  }
}
