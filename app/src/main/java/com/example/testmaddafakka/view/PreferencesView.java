package com.example.testmaddafakka.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.testmaddafakka.model.ICategory;
import com.example.testmaddafakka.R;
import com.example.testmaddafakka.viewmodel.PreferencesViewModel;

import java.util.List;


public class PreferencesView extends Fragment {

    private View view;
    private Spinner actorSpinner;
    private Spinner directorSpinner;
    private PreferencesViewModel viewModel;
    private List<ICategory> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_preferences_view, container, false);

        viewModel = new ViewModelProvider(this).get(PreferencesViewModel.class);
        viewModel.init(requireContext());
        viewModel.getCategories().observe(getViewLifecycleOwner(), category -> {
            categories = category;
            updateMediaDisplayed(categories);
        });

        actorSpinner = (Spinner) view.findViewById(R.id.actorSpinner);
        ArrayAdapter<CharSequence> actorAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.genres_array, android.R.layout.simple_spinner_item);
        setSpinner(actorSpinner, actorAdapter);

        directorSpinner = (Spinner) view.findViewById(R.id.directorSpinner);
        ArrayAdapter<CharSequence> directorAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.genres_array, android.R.layout.simple_spinner_item);
        setSpinner(directorSpinner, directorAdapter);



        Spinner spinner = view.findViewById(R.id.genreSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String genre = spinner.getSelectedItem().toString();
                viewModel.loadSelectedCategory(genre);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        return view;
    }
    public void setSpinner(Spinner spinner, ArrayAdapter<CharSequence> content){
        // Specify the layout to use when the list of choices appears
        content.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(content);
    }

    private void updateMediaDisplayed(List<ICategory> categories) {
        Spinner spinner = view.findViewById(R.id.genreSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        for(int i = 0; i < categories.size(); i++){
            spinnerAdapter.add(categories.get(i).getName());
        }
        spinnerAdapter.notifyDataSetChanged();
    }
}