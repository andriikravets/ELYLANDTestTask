import java.util.LinkedList;
import java.util.List;

public class AddressBook {

    public static final String SWISS_CODE = "070";
    private AddressDb addressDb = AddressDb.getInstance();

    /**
     * @param name specified name of the person
     * @return information has person mobile number or not
     */
    public boolean hasMobile(String name) {
        final Person person = addressDb.findPerson(name);
        return person != null && hasSwissMobileNumber(person);
    }

    /**
     * @return quantity of the people stored in database
     */
    public int getSize() {
        return addressDb.getAll().size();
    }

    /**
     * @param name the name of the required person
     * @return the given user's mobile phone number,
     * or null if he doesn't have one.
     */
    public String getMobile(String name) {
        Person person = addressDb.findPerson(name);
        return hasMobile(name) ? person.getPhoneNumber() : null;
    }

    /**
     * @param maxLength specify maximum length of the person name
     * @return all names in the book truncated to the given length.
     */
    public List getNames(int maxLength) {
        List<String> names = new LinkedList<>();
        for (Person person : addressDb.getAll()) {
            String name = person.getName();
            if (name.length() > maxLength) {
                names.add(name.substring(0, maxLength));
            }
        }
        return names;
    }

    /**
     * @return all people who have mobile phone numbers.
     */
    public List getList() {
        List<Person> listOfPersonWithMobilePhones = new LinkedList<>();
        for (Person person : addressDb.getAll()) {
            if (hasMobile(person.getName())) {
                listOfPersonWithMobilePhones.add(person);
            }
        }
        return listOfPersonWithMobilePhones;
    }

    private boolean hasSwissMobileNumber(Person person) {
        return person.getPhoneNumber().startsWith(SWISS_CODE);
    }

}
