package ru.otus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.otus.АТМ.safe;

import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class АТМTest {

    static Map<Note, Integer> testSafeMap;
    АТМ АТМ;
    Integer NOTES_SIZE = 8;// количество номиналов банкнот (ячеек) всего
    Integer SUM_TO_GETTING = 2310;
    Integer SUM_TO_GETTING_EXCEPTION = 4490;
    Integer ACCUM_ACCOUNT_SUM = 14380;
    Integer DEFERENCE = 12070;

    @BeforeAll
    static void initNoteMapping(){
        testSafeMap = new TreeMap<>();
        testSafeMap.put(Note.TEN, 3);
        testSafeMap.put(Note.FIFTY, 1);
        testSafeMap.put(Note.HUNDRED, 2);
        testSafeMap.put(Note.TWO_HUNDRED, 3);
        testSafeMap.put(Note.FIVE_HUNDRED, 1);
        testSafeMap.put(Note.THOUSAND, 1);
        testSafeMap.put(Note.TWO_THOUSAND, 1);
        testSafeMap.put(Note.FIVE_THOUSAND, 2);
    }

    @BeforeEach
    void initBankAccount(){// 10'000
        АТМ = new АТМ();
        safe = new TreeMap<>();
        safe.put(Note.TEN, 3);
        safe.put(Note.FIFTY, 1);
        safe.put(Note.HUNDRED, 2);
        safe.put(Note.TWO_HUNDRED, 3);
        safe.put(Note.FIVE_HUNDRED, 1);
        safe.put(Note.THOUSAND, 1);
        safe.put(Note.TWO_THOUSAND, 1);
        safe.put(Note.FIVE_THOUSAND, 2);
    }

    @Test
    void addToSafe() {
        АТМ.addToSafe(testSafeMap);
        testSafeMap.forEach((key, value) -> assertThat(value).isEqualTo(safe.get(key)));
    }


    @Test
    void lookSafe() {
        assertThat(NOTES_SIZE).isEqualTo(АТМ.lookSafe().values().size());
    }


    @Test
    void takeFromSafeAndAccumulate() throws IllegalAccessException {
        АТМ.takeFromSafe(SUM_TO_GETTING);
        assertThat(DEFERENCE).isEqualTo(АТМ.accumulateLookSafe());
    }

    @Test
    @Disabled
    void accumulateLookSafe() {
        assertThat(ACCUM_ACCOUNT_SUM).isEqualTo(АТМ.accumulateLookSafe());
    }

    @Test
    void takeFromSafeException() {
        Assertions.assertThrows(IllegalAccessException.class,
                () -> АТМ.takeFromSafe(SUM_TO_GETTING_EXCEPTION));
    }


    @AfterEach
    void clearSafe(){
        safe = new HashMap<>();
    }
}