package es.iessaladillo.pedrojoya.greet;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import es.iessaladillo.pedrojoya.greet.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {

    private final int MAX_GREET = 10;
    private MainActivityBinding binding;
    private int countGreet = 0;
    private RadioButton currentTreatment;
    private boolean isPremium = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViews();
    }

    private void setupViews() {
        setupListeners();
        setDefaults();
        showProgress();
    }


    private void setupListeners() {
        binding.btnGreet.setOnClickListener(v -> greet());
        binding.switchPremium.setOnCheckedChangeListener((compoundButton, b) -> setProgressVisibility(b));
        binding.rdgTreatment.setOnCheckedChangeListener((radioGroup, i) -> setTreatment(radioGroup));
        binding.edtSirname.setOnEditorActionListener((v, actionId, event) -> {
            greet();
            return true;
        });
    }

    private void setDefaults() {
        binding.prBarGreet.setMax(MAX_GREET);
        binding.rdgTreatment.check(R.id.rdb_mr);
    }

    private void showProgress() {
        binding.prBarGreet.setProgress(countGreet);
        binding.lblCountGreet.setText(getString(R.string.lbl_count_greet, countGreet, MAX_GREET));
    }

    private void setProgressVisibility(boolean isChecked) {
        countGreet = 0;
        isPremium = isChecked;
        binding.prBarGreet.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        binding.lblCountGreet.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        if (!isChecked) showProgress();
    }

    private void setTreatment(RadioGroup radioGroup) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rdb_mr:
                setImgTreatment(R.drawable.ic_mr);
                break;
            case R.id.rdb_mrs:
                setImgTreatment(R.drawable.ic_mrs);
                break;
            case R.id.rdb_ms:
                setImgTreatment(R.drawable.ic_ms);
                break;
            default:
                throw new RuntimeException("RadioButton id not found");
        }
        currentTreatment = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
    }

    private void setImgTreatment(int drawable) {
        binding.imgTreatment.setImageResource(drawable);
    }

    private void greet() {
        String name = binding.edtName.getText().toString();
        String sirname = binding.edtSirname.getText().toString();

        if (isValidForm(name, sirname)) {

            SoftInputUtils.hideSoftKeyboard(binding.edtName);

            if (countGreet >= MAX_GREET) {
                showMessage(getString(R.string.message_buy_premium));
                return;
            }

            if (!isPremium) {
                countGreet += 1;
                showProgress();
            }
            showGreet(name, sirname);
        }
    }

    private void showGreet(String name, String sirname) {
        String treatment = currentTreatment.getText().toString();
        showMessage(binding.chkPolitely.isChecked() ?
                getString(R.string.lbl_greet_politely, treatment, name, sirname) : getString(R.string.lbl_greet_no_politely, name, sirname));
    }

    private boolean isValidForm(String name, String sirname) {
        return isNotBlankOrEmpty(name) && isNotBlankOrEmpty(sirname);
    }

    private void showMessage(String message) {
        binding.lblGreet.setText(message);
    }

    /**
     * Check if the string is valid ( it is not empty and it does not start with blank spaces and it does not end with blank spaces).
     *
     * @param str string to check if it is valid.
     * @return true if the string is valid or false if the string is not valid.
     */
    private boolean isNotBlankOrEmpty(String str) {
        return !str.isEmpty() && !str.startsWith(" ") && !str.endsWith(" ");
    }


}