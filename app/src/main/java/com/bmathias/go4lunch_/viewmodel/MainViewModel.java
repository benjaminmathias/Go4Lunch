package com.bmathias.go4lunch_.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.repositories.UserRepository;

public class MainViewModel extends ViewModel {

   private final UserRepository userRepository;

   public MainViewModel(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   public void checkIfUserIsLoggedIn(){
      userRepository.isCurrentUserLogged();
   }

   public void getCurrentUser(){
      userRepository.getCurrentUser();
   }
/*
   public void signOutUser(){
      userRepository.signOut(Context context);
   }*/
}
