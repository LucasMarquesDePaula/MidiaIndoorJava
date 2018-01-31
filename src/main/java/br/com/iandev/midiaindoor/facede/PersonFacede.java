package br.com.iandev.midiaindoor.facede;

import br.com.iandev.midiaindoor.dao.Dao;
import br.com.iandev.midiaindoor.dao.PersonDao;
import br.com.iandev.midiaindoor.model.Device;
import br.com.iandev.midiaindoor.model.Person;
import br.com.iandev.midiaindoor.settings.LicenseSettings;
import org.hibernate.Transaction;

public final class PersonFacede extends CrudFacede<Person> {

    public PersonFacede() {
        super(new PersonDao());
    }

    public PersonFacede(Transaction transaction) {
        super(new PersonDao(), transaction);
    }

    @Override
    protected void beforeSet(Person model, Dao<Person> dao, Transaction transaction) throws ValidationException {
        LicenseSettings licenseSettings = new LicenseSettings();
        Long personId = Long.parseLong(licenseSettings.getOwnerId());
        if (!personId.equals(model.getId())) {
            throw new ValidationException();
        }
    }

    @Override
    protected void beforeDelete(Person model, Dao<Person> dao, Transaction transaction) throws IllegalOperationException {
        try {
            DeviceFacede facede = new DeviceFacede(transaction);
            Device device = facede.get();
            device.setPerson(null);
            facede.save(device);
        } catch (NotFoundException ignored) {
//            Silence is golden
        } catch (Exception ex) {
            throw new IllegalOperationException(ex.getMessage());
        }
    }
}
