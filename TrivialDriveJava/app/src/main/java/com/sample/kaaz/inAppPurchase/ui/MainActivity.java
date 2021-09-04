/*
 * Copyright (C) 2021 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample.kaaz.inAppPurchase.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.sample.kaaz.inAppPurchase.BuildConfig;
import com.sample.kaaz.inAppPurchase.MainActivityViewModel;
import com.sample.kaaz.inAppPurchase.R;
import com.sample.kaaz.inAppPurchase.InAppPurchaseApplication;
import com.sample.kaaz.inAppPurchase.databinding.ActivityMainBinding;

/*
 * MainActivity here exists as a container for the fragments that display the various bits of UI,
 * as well as the CoordinatorLayout/SnackBar implementation.
 */
public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MainActivity", "onCreate");
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        // Setup toolbar with nav controller
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        // Create our Activity ViewModel, which exists to handle global Snackbar messages
        MainActivityViewModel.MainActivityViewModelFactory mainActivityViewModelFactory = new
                MainActivityViewModel.MainActivityViewModelFactory(
                ((InAppPurchaseApplication) getApplication()).appContainer.
                        trivialDriveRepository);
        mainActivityViewModel = new ViewModelProvider(this, mainActivityViewModelFactory)
                .get(MainActivityViewModel.class);
        mainActivityViewModel.getMessages().observe(this, resId -> {
            Snackbar snackbar = Snackbar.make(activityMainBinding.mainLayout, getString(resId),
                    Snackbar.LENGTH_SHORT);
            snackbar.show();
        });
        // Allows billing to refresh purchases during onResume
        getLifecycle().addObserver(mainActivityViewModel.getBillingLifecycleObserver());

        // A helpful hint to prevent confusion when billing transactions silently fail
        if ( BuildConfig.BASE64_ENCODED_PUBLIC_KEY.equals("null")) {
            if ( getSupportFragmentManager()
                    .findFragmentByTag(PublicKeyNotSetDialog.DIALOG_TAG) == null ) {
                new PublicKeyNotSetDialog()
                        .show(getSupportFragmentManager(), PublicKeyNotSetDialog.DIALOG_TAG);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // we could nicely disable this when we don't have a premium purchase by observing
        // the LiveData for the SKU_GAS, but it's just there for testing
        if (item.getItemId() == R.id.menu_consume_premium) {
            mainActivityViewModel.debugConsumePremium();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public static class PublicKeyNotSetDialog extends DialogFragment {
        static final String DIALOG_TAG = "PublicKeyNotSetDialog";
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.alert_error_title_encoded_public_key_not_set)
                    .setMessage(
                            R.string.alert_error_message_encoded_public_key_not_set)
                    .setPositiveButton(getString(android.R.string.ok),
                            (dialog, which) -> {
                            })
                    .create();
        }
    }
}
