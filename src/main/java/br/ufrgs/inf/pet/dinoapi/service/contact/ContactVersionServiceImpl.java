package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.ContactVersion;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

@Service
public class ContactVersionServiceImpl {

    @Autowired
    ContactVersionRepository contactVersionRepository;
    @Autowired
    UserServiceImpl userServiceImpl;

    public void updateVersion(User user) {

        ContactVersion version = user.getContactVersion();

        if (version == null) {
            version = new ContactVersion();
            version.setVersion(0L);
            version.setUser(user);
        } else {
            version.setVersion(version.getVersion() + 1);
        }

        contactVersionRepository.save(version);
    }

    public ResponseEntity<Long> getVersion() {

        User user = userServiceImpl.getCurrentUser();

        ContactVersion version = user.getContactVersion();

        if (version == null) {
            version = new ContactVersion();
            version.setVersion(0L);
            version.setUser(user);

            contactVersionRepository.save(version);
        }

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

}
