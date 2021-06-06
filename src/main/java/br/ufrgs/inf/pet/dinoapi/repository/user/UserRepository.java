package br.ufrgs.inf.pet.dinoapi.repository.user;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.userSettings.includeEssentialContact = true AND u.permission = :permission")
    List<User> findWhoCanHaveEssentialContacts(String permission);

    @Query("SELECT u FROM User u " +
            "WHERE u.userSettings.includeEssentialContact = true " +
            "AND u.permission = :permission " +
            "AND u.userSettings.treatment IN :treatments")
    List<User> findWhoCanHaveEssentialContacts(List<Treatment> treatments, String permission);

    @Query("SELECT u FROM User u " +
            "WHERE u.userSettings.includeEssentialContact = true " +
            "AND u.permission = :permission " +
            "AND (SELECT COUNT(c) FROM Contact c WHERE c.user = u AND c.essentialContact = :essentialContact) = 0")
    List<User> findWhoCanHaveEssentialContactsButDontHave(EssentialContact essentialContact, String permission);

    @Query("SELECT u FROM User u " +
            "WHERE u.userSettings.includeEssentialContact = true " +
            "AND u.permission = :permission " +
            "AND u.userSettings.treatment IN :treatments " +
            "AND (SELECT COUNT(c) FROM Contact c WHERE c.user = u AND c.essentialContact = :essentialContact) = 0")
    List<User> findWhoCanHaveEssentialContactsButDontHave(List<Treatment> treatments,
                                               EssentialContact essentialContact, String permission);
}
