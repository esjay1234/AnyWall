package com.parse.anywall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.parse.ParseUser;

import java.util.Collections;
import java.util.List;

/**
 * Activity that displays the settings screen.
 */
public class SettingsActivity extends Activity {

  private List<Float> availableOptions = Application.getConfigHelper().getSearchDistanceAvailableOptions();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_settings);

    float currentSearchDistance = Application.getSearchDistance();
    int currentSelected=Application.getClicked();
    if (!availableOptions.contains(currentSearchDistance)) {
      availableOptions.add(currentSearchDistance);
    }
    Collections.sort(availableOptions);

    // The search distance choices
    RadioGroup searchDistanceRadioGroup = (RadioGroup) findViewById(R.id.searchdistance_radiogroup);
    RadioGroup searchCatRadioGroup=(RadioGroup) findViewById(R.id.searchCat_radiogroup);
    for (int index = 0; index < availableOptions.size(); index++) {
      float searchDistance = availableOptions.get(index);

      RadioButton button = new RadioButton(this);
      button.setId(index);
      button.setText(getString(R.string.settings_distance_format, (int)searchDistance));
      searchDistanceRadioGroup.addView(button, index);

      if (currentSearchDistance == searchDistance) {
        searchDistanceRadioGroup.check(index);
      }
    }

    String []options={"View only HELPERS","View only people who need HELP","View ALL"};


    for (int i=0; i<3; i++) {
        RadioButton button = new RadioButton(this);
        button.setId(i);
        button.setText(options[i]);
        searchCatRadioGroup.addView(button, i);
        if (currentSelected == i) {
            searchCatRadioGroup.check(i);
        }
    }
      searchCatRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {
              Application.setClicked(checkedId);
          }
      });


    // Set up the selection handler to save the selection to the application
    searchDistanceRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        Application.setSearchDistance(availableOptions.get(checkedId));
      }
    });

    // Set up the log out button click handler
    Button logoutButton = (Button) findViewById(R.id.logout_button);
    logoutButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        // Call the Parse log out method
        ParseUser.logOut();
        // Start and intent for the dispatch activity
        Intent intent = new Intent(SettingsActivity.this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
      }
    });
  }
}
