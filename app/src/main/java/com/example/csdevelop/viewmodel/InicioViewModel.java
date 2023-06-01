package com.example.csdevelop.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.csdevelop.model.Concierto;

import java.util.List;

public class InicioViewModel extends ViewModel {
    private MutableLiveData<List<String>> data;
    private MutableLiveData<Concierto> dataConcierto;

    private Concierto concierto;

    public void init(){
        concierto = new Concierto();

    }
}
