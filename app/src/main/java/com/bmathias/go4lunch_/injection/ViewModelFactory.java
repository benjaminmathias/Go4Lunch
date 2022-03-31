package com.bmathias.go4lunch_.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch_.viewmodel.DetailsViewModel;
import com.bmathias.go4lunch_.viewmodel.ListViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

   private final RestaurantRepository restaurantDatasource;

   public ViewModelFactory(RestaurantRepository restaurantDatasource) {
      this.restaurantDatasource = restaurantDatasource;
   }

   @NonNull
   @Override
   public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
      if(aClass.isAssignableFrom(ListViewModel.class)){
         return (T) new ListViewModel(restaurantDatasource);
      } else if(aClass.isAssignableFrom(DetailsViewModel.class)){
         return (T) new DetailsViewModel(restaurantDatasource);
      }
      throw new IllegalArgumentException("Unknown ViewModel class");
   }
}
