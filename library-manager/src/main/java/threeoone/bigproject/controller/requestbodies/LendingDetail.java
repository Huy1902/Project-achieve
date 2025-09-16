package threeoone.bigproject.controller.requestbodies;

import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.Member;

public record LendingDetail(Member member, Document document) {
}
