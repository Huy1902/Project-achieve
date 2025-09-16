package threeoone.bigproject.repositories;

import org.springframework.data.repository.ListCrudRepository;
import threeoone.bigproject.entities.LendingDetail;

public interface LendingDetailRepo extends ListCrudRepository <LendingDetail, Integer> {

}
