package burger;

import org.assertj.core.api.SoftAssertions;
import praktikum.IngredientType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.regex.Pattern;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BurgerGetReceiptTest extends BurgerBaseTest {

    @Before
    public void stubForReceipt() {
        //Стабим то, что нужно для тестов
        when(mockBun.getPrice()).thenReturn(100.0f);
        when(mockBun.getName()).thenReturn("Mock Bun");

        when(mockFilling.getName()).thenReturn("Default Filling");
        when(mockFilling.getType()).thenReturn(IngredientType.FILLING);
        when(mockFilling.getPrice()).thenReturn(30f);
    }

//__Метод getReceipt____________________________________________________________________________________________________

    // Формат начала и конца чека
    @Test
    public void getReceiptShouldHaveCorrectHeaderTest() {

        burger.addIngredient(mockFilling);
        String receipt = burger.getReceipt();

        //Строим строки в том же формате, что в getReceipt
        String expectedHeader = String.format("(==== %s ====)%n", mockBun.getName());
        String expectedEnding = String.format("%nPrice: %f%n", burger.getPrice());

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(receipt.startsWith(expectedHeader))
                .as("Чек должен строго начинаться с шапки с дальнейшим переносом строки")
                .isTrue();

        soft.assertThat(receipt.endsWith(expectedEnding))
                .as("Чек должен строго заканчиваться строкой цены с переносом строки")
                .isTrue();
        soft.assertAll();
    }

    //Футер - позиция: подвал идёт после ингредиентов и перед ценой
    @Test
    public void getReceiptShouldPlaceFooterAfterIngredientsTest() {

        burger.addIngredient(mockFilling);
        String receipt = burger.getReceipt();

        String bunLine = String.format("(==== %s ====)%n", mockBun.getName());
        String ingredientLine = String.format("= %s %s =%n",
                mockFilling.getType().toString().toLowerCase(),
                mockFilling.getName());

        String priceLine = String.format("Price: %f", burger.getPrice());

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(receipt.lastIndexOf(bunLine) < receipt.indexOf(priceLine))
                .as("Подвал (вторая строка булки) должен идти до строки цены")
                .isTrue();

        soft.assertThat(receipt.lastIndexOf(bunLine) > receipt.indexOf(ingredientLine))
                .as("Подвал (вторая строка булки) должен идти после строки ингредиента")
                .isTrue();
        soft.assertAll();
    }


    @Test
    public void getReceiptShouldContainBunLineTwiceTest() {

        String receipt = burger.getReceipt();
        String bunLine = String.format("(==== %s ====)%n", mockBun.getName());

        //Счетчик, сколько раз появилось bunLine в receipt
        long bunLineCount = Pattern.compile(Pattern.quote(bunLine))
                .matcher(receipt)
                .results()
                .count();

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(receipt)
                .as("Чек должен содержать строку булки")
                .contains(bunLine);
        soft.assertThat(bunLineCount)
                .as("Строка булки должна встречаться ровно 2 раза (шапка и подвал)")
                .isEqualTo(2);
        soft.assertAll();
    }

    //Формат цены
    @Test
    public void getReceiptShouldContainPriceLineTest() {

        burger.addIngredient(mockFilling);
        String receipt = burger.getReceipt();

        String expectedPriceLine = String.format("Price: %f", burger.getPrice());

        assertTrue("В чеке должна быть строка цены в ожидаемом формате",
                receipt.contains(expectedPriceLine));
    }

    @Test
    public void getReceiptShouldHaveExactlyOnePriceLineTest() {

        burger.addIngredient(mockFilling);
        String receipt = burger.getReceipt();

        String expectedPriceLine = String.format("Price: %f", burger.getPrice());

        long priceLineCount = Pattern.compile(Pattern.quote(expectedPriceLine))
                .matcher(receipt)
                .results()
                .count();

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(receipt)
                .as("Чек должен содержать строку цены")
                .contains(expectedPriceLine);
        soft.assertThat(priceLineCount)
                .as("Строка цены должна встречаться ровно 1 раз")
                .isEqualTo(1);
        soft.assertAll();
    }

    //Строка ингредиента
    @Test
    public void getReceiptShouldHaveCorrectIngredientFormatTest() {

        burger.addIngredient(mockFilling);
        String receipt = burger.getReceipt();

        String expectedLine = String.format("= %s %s =%n",
                mockFilling.getType().toString().toLowerCase(),
                mockFilling.getName());

        assertTrue("Строка ингредиента должна точно соответствовать формату",
                receipt.contains(expectedLine));
    }

    @Test
    public void getReceiptWithoutIngredientsShouldNotContainIngredientLineTest() {

        String receipt = burger.getReceipt();

        boolean hasIngredientLine = Pattern.compile("(?m)^= ").matcher(receipt).find();

        assertFalse("В чеке без ингредиентов не должно быть строки ингредиента",
                hasIngredientLine);
    }
}
