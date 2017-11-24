package com.jabwrb.nutridiary;


import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.DatePicker;

import com.jabwrb.nutridiary.activity.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test00() {
        /**
         * Date picker เปลี่ยนเป็นวันที่ 12 December 2017
         * จะต้องเจอ 12 DEC 2017
         */
        onView(withId(R.id.btnDatePicker)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2017, 12, 12));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btnDatePicker)).check(matches(withText("12 Dec 2017")));
    }

    @Test
    public void test01() {
        /**
         * Create food ใส่ข้อมูลครบ
         * จะต้องเจอ 00 Food, 100g, 500 cal ใน RecyclerView เป็นตัวแรก
         */
        onView(withId(R.id.fabAddEntry)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.etName)).perform(replaceText("00 Food"), closeSoftKeyboard());
        onView(withId(R.id.etBrand)).perform(replaceText("NutriDiary"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeUnit)).perform(replaceText("100"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeMeasurement)).perform(replaceText("g"), closeSoftKeyboard());
        onView(withId(R.id.etCalories)).perform(replaceText("500"), closeSoftKeyboard());
        onView(withId(R.id.etFat)).perform(replaceText("10"), closeSoftKeyboard());
        onView(withId(R.id.etSaturatedFat)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etCholesterol)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.etSodium)).perform(replaceText("50"), closeSoftKeyboard());
        onView(withId(R.id.etCarbohydrates)).perform(replaceText("20"), closeSoftKeyboard());
        onView(withId(R.id.etDietaryFiber)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etSugars)).perform(replaceText("2"), closeSoftKeyboard());
        onView(withId(R.id.etProtein)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.action_confirm)).perform(click());

        onView(withId(R.id.listFood)).check(matches(atPosition(0, hasDescendant(withText("00 Food")))));
        onView(withId(R.id.listFood)).check(matches(atPosition(0, hasDescendant(withText("100 g, 500 cal")))));
    }

    @Test
    public void test02() {
        /**
         * Create food ไม่ใส่ Name
         * จะต้องเจอ info_missed_error
         */
        onView(withId(R.id.fabAddEntry)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.etName)).perform(clearText());
        onView(withId(R.id.etBrand)).perform(replaceText("NutriDiary"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeUnit)).perform(replaceText("100"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeMeasurement)).perform(replaceText("g"), closeSoftKeyboard());
        onView(withId(R.id.etCalories)).perform(replaceText("500"), closeSoftKeyboard());
        onView(withId(R.id.etFat)).perform(replaceText("10"), closeSoftKeyboard());
        onView(withId(R.id.etSaturatedFat)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etCholesterol)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.etSodium)).perform(replaceText("50"), closeSoftKeyboard());
        onView(withId(R.id.etCarbohydrates)).perform(replaceText("20"), closeSoftKeyboard());
        onView(withId(R.id.etDietaryFiber)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etSugars)).perform(replaceText("2"), closeSoftKeyboard());
        onView(withId(R.id.etProtein)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.action_confirm)).perform(click());

        onView(withText(R.string.info_missed_error))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        SystemClock.sleep(3500);
    }

    @Test
    public void test03() {
        /**
         * Create food ไม่ใส่ Calories
         * จะต้องเจอ info_missed_error
         */
        onView(withId(R.id.fabAddEntry)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.etName)).perform(replaceText("00 Food"), closeSoftKeyboard());
        onView(withId(R.id.etBrand)).perform(replaceText("NutriDiary"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeUnit)).perform(replaceText("100"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeMeasurement)).perform(replaceText("g"), closeSoftKeyboard());
        onView(withId(R.id.etCalories)).perform(clearText());
        onView(withId(R.id.etFat)).perform(replaceText("10"), closeSoftKeyboard());
        onView(withId(R.id.etSaturatedFat)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etCholesterol)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.etSodium)).perform(replaceText("50"), closeSoftKeyboard());
        onView(withId(R.id.etCarbohydrates)).perform(replaceText("20"), closeSoftKeyboard());
        onView(withId(R.id.etDietaryFiber)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etSugars)).perform(replaceText("2"), closeSoftKeyboard());
        onView(withId(R.id.etProtein)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.action_confirm)).perform(click());

        onView(withText(R.string.info_missed_error))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        SystemClock.sleep(3500);
    }

    @Test
    public void test04() {
        /**
         * Create food ไม่ใส่ Serving Size Unit
         * จะต้องเจอ info_missed_error
         */
        onView(withId(R.id.fabAddEntry)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.etName)).perform(replaceText("00 Food"), closeSoftKeyboard());
        onView(withId(R.id.etBrand)).perform(replaceText("NutriDiary"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeUnit)).perform(clearText());
        onView(withId(R.id.etServingSizeMeasurement)).perform(replaceText("g"), closeSoftKeyboard());
        onView(withId(R.id.etCalories)).perform(replaceText("500"), closeSoftKeyboard());
        onView(withId(R.id.etFat)).perform(replaceText("10"), closeSoftKeyboard());
        onView(withId(R.id.etSaturatedFat)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etCholesterol)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.etSodium)).perform(replaceText("50"), closeSoftKeyboard());
        onView(withId(R.id.etCarbohydrates)).perform(replaceText("20"), closeSoftKeyboard());
        onView(withId(R.id.etDietaryFiber)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etSugars)).perform(replaceText("2"), closeSoftKeyboard());
        onView(withId(R.id.etProtein)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.action_confirm)).perform(click());

        onView(withText(R.string.info_missed_error))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        SystemClock.sleep(3500);
    }

    @Test
    public void test05() {
        /**
         * Create food ไม่ใส่ Serving Size Measurement
         * จะต้องเจอ info_missed_error
         */
        onView(withId(R.id.fabAddEntry)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.etName)).perform(replaceText("00 Food"), closeSoftKeyboard());
        onView(withId(R.id.etBrand)).perform(replaceText("NutriDiary"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeUnit)).perform(replaceText("100"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeMeasurement)).perform(clearText());
        onView(withId(R.id.etCalories)).perform(replaceText("500"), closeSoftKeyboard());
        onView(withId(R.id.etFat)).perform(replaceText("10"), closeSoftKeyboard());
        onView(withId(R.id.etSaturatedFat)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etCholesterol)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.etSodium)).perform(replaceText("50"), closeSoftKeyboard());
        onView(withId(R.id.etCarbohydrates)).perform(replaceText("20"), closeSoftKeyboard());
        onView(withId(R.id.etDietaryFiber)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etSugars)).perform(replaceText("2"), closeSoftKeyboard());
        onView(withId(R.id.etProtein)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.action_confirm)).perform(click());

        onView(withText(R.string.info_missed_error))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        SystemClock.sleep(3500);
    }

    @Test
    public void test06() {
        /**
         * Create food Name กับ Brand ซ้ำ
         * จะต้องเจอ duplicate_food_error
         */
        onView(withId(R.id.fabAddEntry)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.etName)).perform(replaceText("00 Food"), closeSoftKeyboard());
        onView(withId(R.id.etBrand)).perform(replaceText("NutriDiary"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeUnit)).perform(replaceText("100"), closeSoftKeyboard());
        onView(withId(R.id.etServingSizeMeasurement)).perform(replaceText("g"), closeSoftKeyboard());
        onView(withId(R.id.etCalories)).perform(replaceText("500"), closeSoftKeyboard());
        onView(withId(R.id.etFat)).perform(replaceText("10"), closeSoftKeyboard());
        onView(withId(R.id.etSaturatedFat)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etCholesterol)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.etSodium)).perform(replaceText("50"), closeSoftKeyboard());
        onView(withId(R.id.etCarbohydrates)).perform(replaceText("20"), closeSoftKeyboard());
        onView(withId(R.id.etDietaryFiber)).perform(replaceText("5"), closeSoftKeyboard());
        onView(withId(R.id.etSugars)).perform(replaceText("2"), closeSoftKeyboard());
        onView(withId(R.id.etProtein)).perform(replaceText("30"), closeSoftKeyboard());
        onView(withId(R.id.action_confirm)).perform(click());

        onView(withText(R.string.duplicate_food_error))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        SystemClock.sleep(3500);
    }

    @Test
    public void test07() {
        /**
         * Add to diary เพิ่ม 00 Food, 2.5 serves, มื้อ Breakfast
         * จะต้องเจอ 00 Food, 250 g, 1250 ใน RecyclerView เป็นตัวแรก
         * รวมทั้งหมด 1250 calories
         */
        addFoodEntry("00 Food", "2.5", "Breakfast");

        onView(withId(R.id.tvCalBreakfast)).check(matches(withText("1250 calories")));
        onView(withId(R.id.listBreakfast)).check(matches(atPosition(0, hasDescendant(withText("00 Food")))));
        onView(withId(R.id.listBreakfast)).check(matches(atPosition(0, hasDescendant(withText("250 g")))));
        onView(withId(R.id.listBreakfast)).check(matches(atPosition(0, hasDescendant(withText("1250")))));
    }

    @Test
    public void test08() {
        /**
         * Add to diary เพิ่ม 00 Food, 1.0 serve, มื้อ Lunch
         * จะต้องเจอ 00 Food, 100 g,500 ใน RecyclerView เป็นตัวแรก
         * รวมทั้งหมด 500 calories
         */
        addFoodEntry("00 Food", "1.0", "Lunch");

        onView(withId(R.id.tvCalLunch)).check(matches(withText("500 calories")));
        onView(withId(R.id.listLunch)).check(matches(atPosition(0, hasDescendant(withText("00 Food")))));
        onView(withId(R.id.listLunch)).check(matches(atPosition(0, hasDescendant(withText("100 g")))));
        onView(withId(R.id.listLunch)).check(matches(atPosition(0, hasDescendant(withText("500")))));
    }

    @Test
    public void test09() {
        /**
         * Add to diary เพิ่ม 00 Food, 1.0 serve, มื้อ Dinner
         * จะต้องเจอ 00 Food, 100 g,500 ใน RecyclerView เป็นตัวแรก
         * รวมทั้งหมด 500 calories
         */
        addFoodEntry("00 Food", "1.0", "Dinner");

        onView(withId(R.id.tvCalDinner)).check(matches(withText("500 calories")));
        onView(withId(R.id.listDinner)).check(matches(atPosition(0, hasDescendant(withText("00 Food")))));
        onView(withId(R.id.listDinner)).check(matches(atPosition(0, hasDescendant(withText("100 g")))));
        onView(withId(R.id.listDinner)).check(matches(atPosition(0, hasDescendant(withText("500")))));
    }

    @Test
    public void test10() {
        /**
         * Add to diary เพิ่ม 00 Food, 1.0 serve, มื้อ Snack
         * จะต้องเจอ 00 Food, 100 g,500 ใน RecyclerView เป็นตัวแรก
         * รวมทั้งหมด 500 calories
         */
        addFoodEntry("00 Food", "1.0", "Snack");

        onView(withId(R.id.tvCalSnack)).check(matches(withText("500 calories")));
        onView(withId(R.id.listSnack)).check(matches(atPosition(0, hasDescendant(withText("00 Food")))));
        onView(withId(R.id.listSnack)).check(matches(atPosition(0, hasDescendant(withText("100 g")))));
        onView(withId(R.id.listSnack)).check(matches(atPosition(0, hasDescendant(withText("500")))));
    }

    @Test
    public void test11() {
        /**
         * Add to diary ไม่ใส่ amount
         * จะต้องเจอ amount_missed_error
         */
        addFoodEntry("00 Food", "", "Snack");

        onView(withText(R.string.amount_missed_error))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        SystemClock.sleep(3500);
    }

    @Test
    public void test12() {
        /**
         * Delete Food Entry ของ Breakfast ตัวแรก
         * จะต้องเจอ deleted
         */
        onView(withId(R.id.listBreakfast)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withText("Yes")).perform(click());

        onView(withText(R.string.deleted))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        SystemClock.sleep(3500);
    }

    @Test
    public void test13() {
        /**
         * Search "egg" เพิ่ม Bread, egg, 1.0 serve มื้อ Breakfast
         * จะต้องเจอ Bread, egg, 100 g, 287
         * รวมทั้งหมด 287 calories
         */
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(replaceText("egg"));
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        SystemClock.sleep(10000);
        onView(withId(R.id.listFood)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Bread, egg")), click()));
        onView(withId(R.id.etAmount)).perform(replaceText("1.0"), closeSoftKeyboard());
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Breakfast"))).perform(click());
        onView(withId(R.id.action_add)).perform(click());

        onView(withId(R.id.tvCalBreakfast)).check(matches(withText("287 calories")));
        onView(withId(R.id.listBreakfast)).check(matches(atPosition(0, hasDescendant(withText("Bread, egg")))));
        onView(withId(R.id.listBreakfast)).check(matches(atPosition(0, hasDescendant(withText("100 g")))));
        onView(withId(R.id.listBreakfast)).check(matches(atPosition(0, hasDescendant(withText("287")))));
    }

    @Test
    public void test14() {
        /**
         * Edit food เปลี่ยน Name เป็น 00 My Food
         * จะต้องเจอ จะต้องเจอ 00 My Food, 100g, 500 cal ใน RecyclerView เป็นตัวแรก
         */
        onView(withId(R.id.fabAddEntry)).perform(click());
        onView(withId(R.id.listFood)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.imgBtnInfo)));
        onView(withId(R.id.etName)).perform(replaceText("00 My Food"));
        onView(withId(R.id.action_confirm)).perform(click());

        onView(withText(R.string.updated))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.listFood)).check(matches(atPosition(0, hasDescendant(withText("00 My Food")))));
        onView(withId(R.id.listFood)).check(matches(atPosition(0, hasDescendant(withText("100 g, 500 cal")))));

        SystemClock.sleep(3500);
    }

    private void addFoodEntry(String food, String amount, String meal) {
        onView(withId(R.id.fabAddEntry)).perform(click());
        onView(withId(R.id.listFood)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(food)), click()));
        onView(withId(R.id.etAmount)).perform(replaceText(amount), closeSoftKeyboard());
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(meal))).perform(click());
        onView(withId(R.id.action_add)).perform(click());
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}
