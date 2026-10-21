# Unit-тесты класса Burger

Юнит-тесты для класса `Burger` (проект Praktikum): проверяются цены, рецепт и добавление ингредиентов. Для покрытия используется JaCoCo.

## Технологии

- Java 11, JUnit 4
- Mockito — подмены зависимостей
- JaCoCo — отчёт о покрытии

## Структура классов

**src/main/java/praktikum — тестируемые классы**

- `Bun` — булочка: имя и цена.
- `Ingredient` — ингредиент: тип, имя, цена.
- `IngredientType` — enum типов ингредиентов (SAUCE, FILLING).
- `Database` — доступные для сборки булочки и ингредиенты.
- `Burger` — собираемый бургер: `setBuns`, `addIngredient`, `getPrice`, `getReceipt`.
- `Praktikum` — точка входа программы.

**src/test/java/burger — автотесты**

- `BurgerBaseTest` — общая подготовка моков (`Bun`, `Ingredient`) для всех тестов класса Burger.
- `BurgerPriceParametrizedTest` — параметризованный расчёт цены: разные наборы булочек и ингредиентов.
- `BurgerIngredientsTest` — добавление ингредиентов, перемещение и удаление из списка.
- `BurgerGetReceiptTest` — формат чека: название, состав, разделители, порядок строк.

## Запуск тестов

```bash
mvn clean test
