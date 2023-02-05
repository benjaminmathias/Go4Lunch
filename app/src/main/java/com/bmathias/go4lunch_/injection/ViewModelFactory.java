package com.bmathias.go4lunch_.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.data.repositories.AuthRepository;
import com.bmathias.go4lunch_.data.repositories.ConfigRepository;
import com.bmathias.go4lunch_.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch_.data.repositories.RestaurantRepository;
import com.bmathias.go4lunch_.data.repositories.UsersRepository;
import com.bmathias.go4lunch_.viewmodel.AuthViewModel;
import com.bmathias.go4lunch_.viewmodel.DetailsViewModel;
import com.bmathias.go4lunch_.viewmodel.ListViewModel;
import com.bmathias.go4lunch_.viewmodel.MainViewModel;
import com.bmathias.go4lunch_.viewmodel.MapViewModel;
import com.bmathias.go4lunch_.viewmodel.SettingsViewModel;
import com.bmathias.go4lunch_.viewmodel.SplashViewModel;
import com.bmathias.go4lunch_.viewmodel.WorkmatesViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

   private final RestaurantRepository restaurantDatasource;
   private final AuthRepository authDatasource;
   private final CurrentUserRepository currentUserDatasource;
   private final UsersRepository usersDatasource;
   private final ConfigRepository configRepository;

   public ViewModelFactory(RestaurantRepository restaurantDatasource,
                           AuthRepository authDatasource,
                           CurrentUserRepository currentUserDatasource,
                           UsersRepository usersDatasource,
                           ConfigRepository configRepository) {
      this.restaurantDatasource = restaurantDatasource;
      this.authDatasource = authDatasource;
      this.currentUserDatasource = currentUserDatasource;
      this.usersDatasource = usersDatasource;
      this.configRepository = configRepository;
   }

   @NonNull
   @Override
   @SuppressWarnings("unchecked")
   public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
      if(aClass.isAssignableFrom(ListViewModel.class)){
         return (T) new ListViewModel(restaurantDatasource);
      } else if(aClass.isAssignableFrom(DetailsViewModel.class)){
         return (T) new DetailsViewModel(restaurantDatasource, currentUserDatasource, usersDatasource, configRepository);
      } else if (aClass.isAssignableFrom(AuthViewModel.class)){
         return (T) new AuthViewModel(authDatasource);
      } else if (aClass.isAssignableFrom(SplashViewModel.class)){
         return (T) new SplashViewModel(currentUserDatasource);
      } else if (aClass.isAssignableFrom(WorkmatesViewModel.class)){
         return (T) new WorkmatesViewModel(usersDatasource, currentUserDatasource);
      } else if (aClass.isAssignableFrom(MainViewModel.class)){
         return (T) new MainViewModel(currentUserDatasource);
      } else if (aClass.isAssignableFrom(MapViewModel.class)){
         return (T) new MapViewModel(restaurantDatasource);
      } else if (aClass.isAssignableFrom(SettingsViewModel.class)){
         return (T) new SettingsViewModel(configRepository);
      }
      throw new IllegalArgumentException("Unknown ViewModel class");
   }
}
