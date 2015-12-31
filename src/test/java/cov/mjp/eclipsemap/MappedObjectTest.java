package cov.mjp.eclipsemap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.BeforeClass;
import org.junit.Test;

public class MappedObjectTest {
	public int i1 = 34;
	public Integer i2 = 34;
	
	private final static String stringRepresentationOfObama = 
			"President ["
					+ "country=USA, "
					+ "name=Name ["
						+ "title=Mr, "
						+ "forename=Barack, "
						+ "surname=Obama], "
					+ "address=Address ["
						+ "houseNumber=1600, "
						+ "street=Pennsylvania Avenue, "
						+ "town=Washington]]";
	
	private static President presidentObama;
	private static MappedObject mappedPresidentObama;
	
	@BeforeClass
	public static void inaugurate() {
		Name name = new Name(Title.Mr, "Barack", "Obama");
		Address address = new Address(1600, "Pennsylvania Avenue", "Washington");
		presidentObama = new President("USA", name, address);
	}
	
	@BeforeClass
	public static void map_toString_of_President_Obama() {
		mappedPresidentObama = MappedObject.createFromString(stringRepresentationOfObama);
	}

	@Test
	public void eclipse_default_toString_of_President_object_is_as_expected() {
		assertEquals(stringRepresentationOfObama, presidentObama.toString());
	}

	@Test
	public void mapped_object_country_is_USA() {
		assertEquals("USA", mappedPresidentObama.leafFields.get("country"));
	}

	@Test
	public void mapped_object_title_is_Mr() {
		MappedObject name = mappedPresidentObama.treeFields.get("name");
		//Note that this is a String, not an enum.
		assertEquals("Mr", name.leafFields.get("title"));
	}

	@Test
	public void mapped_object_forename_is_Barack() {
		MappedObject name = mappedPresidentObama.treeFields.get("name");
		assertEquals("Barack", name.leafFields.get("forename"));
	}

	@Test
	public void mapped_object_surname_is_Obama() {
		MappedObject name = mappedPresidentObama.treeFields.get("name");
		assertEquals("Obama", name.leafFields.get("surname"));
	}

	@Test
	public void mapped_object_houseNumber_is_a_String() {
		MappedObject name = mappedPresidentObama.treeFields.get("address");
		//Note that this is a String, not an int.
		assertEquals(String.class, name.leafFields.get("houseNumber").getClass());
	}

	@Test
	public void mapped_object_houseNumber_is_1600() {
		MappedObject name = mappedPresidentObama.treeFields.get("address");
		//Note that this is a String, not an int.
		assertEquals("1600", name.leafFields.get("houseNumber"));
	}

	@Test
	public void mapped_object_street_is_Pennsylvania_Avenue() {
		MappedObject name = mappedPresidentObama.treeFields.get("address");
		assertEquals("Pennsylvania Avenue", name.leafFields.get("street"));
	}

	@Test
	public void mapped_object_town_is_Washington() {
		MappedObject name = mappedPresidentObama.treeFields.get("address");
		assertEquals("Washington", name.leafFields.get("town"));
	}
}
