package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;



public class CardDeliveryTest {

    @BeforeEach
    public void setup() {

        open("http://localhost:9999");

    }

    private String getFutureDate(int addDays) {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(addDays);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return futureDate.format(formatter);
    }

    @Test

    public void shouldSubmitRequest() {

        $("[data-test-id=city] input").setValue("Псков");
        $(".calendar-input__custom-control input").doubleClick().setValue(getFutureDate(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=notification] .notification__title")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Успешно!"));
        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible).shouldHave(exactText("Встреча успешно забронирована на " + getFutureDate(3)));
    }

    @Test

    public void shouldNotSubmitIfWrongCity() {

        $("[data-test-id=city] input").setValue("AA");
        $(".calendar-input__custom-control input").doubleClick().setValue(getFutureDate(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test

    public void shouldNotSubmitIfCityEmpty() {

        $(".calendar-input__custom-control input").doubleClick().setValue(getFutureDate(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test

    public void shouldNotSubmitIfDateEmpty() {

        $("[data-test-id=city] input").setValue("Псков");
        $(".calendar-input__custom-control input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(".calendar-input .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test

    public void shouldNotSubmitIfWrongName() {

        $("[data-test-id=city] input").setValue("Псков");
        $(".calendar-input__custom-control input").doubleClick().setValue(getFutureDate(3));
        $("[data-test-id=name] input").setValue("John");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test

    public void shouldNotSubmitIfNameIsEmpty() {

        $("[data-test-id=city] input").setValue("Псков");
        $(".calendar-input__custom-control input").doubleClick().setValue(getFutureDate(3));
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test

    public void shouldNotSubmitIfWrongPhoneNumber() {

        $("[data-test-id=city] input").setValue("Псков");
        $(".calendar-input__custom-control input").doubleClick().setValue(getFutureDate(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test

    public void shouldNotSubmitIfPhoneNumberIsEmpty() {

        $("[data-test-id=city] input").setValue("Псков");
        $(".calendar-input__custom-control input").doubleClick().setValue(getFutureDate(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
//        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test

    public void shouldNotSubmitIfNotSignedAgreement() {
        $("[data-test-id=city] input").setValue("Псков");
        $(".calendar-input__custom-control input").doubleClick().setValue(getFutureDate(3));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $(".button").click();
        $("[data-test-id=agreement].input_invalid").shouldBe(visible);
    }
}
