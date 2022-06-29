package com.example.mr_kotlin.utils

import com.example.mr_kotlin.data.Database
import com.example.mr_kotlin.data.genre.GenreRepository
import com.example.mr_kotlin.data.movie.MovieRepository
import com.example.mr_kotlin.data.user.UserServiceAdapter
import com.example.mr_kotlin.data.user.UserRepository
import com.example.mr_kotlin.ui.edit_profile.EditProfileViewModelFactory
import com.example.mr_kotlin.ui.home.HomeViewModelFactory
import com.example.mr_kotlin.ui.liked_list.LikedListViewModelFactory
import com.example.mr_kotlin.ui.login.LoginViewModelFactory
import com.example.mr_kotlin.ui.movie_detail.MovieDetailViewModelFactory
import com.example.mr_kotlin.ui.profile.ProfileViewModelFactory
import com.example.mr_kotlin.ui.signup.SignupViewModelFactory
import com.example.mr_kotlin.ui.menu.MenuViewModelFactory
import com.example.mr_kotlin.utils.auth.Auth

object InjectorUtils {

    // This will be called from HomeActivity
    fun provideHomeViewModelFactory(): HomeViewModelFactory {
        // ViewModelFactory needs a repository, which in turn needs a DAO from a database
        // The whole dependency tree is constructed right here, in one place
        val userRepository = UserRepository.getInstance(Database.getInstance().userDAO, Database.getInstance().lruChacheUser)
        val movieRepository = MovieRepository.getInstance(Database.getInstance().movieDAO)
        val sensorAdmin = SensorAdmin()
        val auth = Auth()
        return HomeViewModelFactory(userRepository,movieRepository, sensorAdmin, auth)
    }


    // This will be called from Edit Profile
    fun provideEditProfileViewModelFactory(): EditProfileViewModelFactory {
        // ViewModelFactory needs a repository, which in turn needs a DAO from a database
        // The whole dependency tree is constructed right here, in one place
        val userRepository = UserRepository.getInstance(Database.getInstance().userDAO, Database.getInstance().lruChacheUser)
        val movieRepository = MovieRepository.getInstance(Database.getInstance().movieDAO)
        val sensorAdmin = SensorAdmin()
        val userDAO = UserServiceAdapter()
        val auth = Auth()
        return EditProfileViewModelFactory(userRepository,movieRepository, sensorAdmin,userDAO, auth)
    }


    // This will be called from Liked Acticity
    fun provideLikedListViewModelFactory(): LikedListViewModelFactory{
        // ViewModelFactory needs a repository, which in turn needs a DAO from a database
        // The whole dependency tree is constructed right here, in one place
        val userRepository = UserRepository.getInstance(Database.getInstance().userDAO, Database.getInstance().lruChacheUser)
        val movieRepository = MovieRepository.getInstance(Database.getInstance().movieDAO)
        val sensorAdmin = SensorAdmin()
        val auth = Auth()
        return LikedListViewModelFactory(userRepository,movieRepository, sensorAdmin, auth)
    }

    // This will be called from LoginActivity
    fun provideLoginViewModelFactory(): LoginViewModelFactory {
        // ViewModelFactory needs a repository, which in turn needs a DAO from a database
        // The whole dependency tree is constructed right here, in one place
        val auth = Auth()
        return LoginViewModelFactory(auth)
    }

    // This will be called from SignupActivity
    fun provideSignupViewModelFactory(): SignupViewModelFactory {
        // ViewModelFactory needs a repository, which in turn needs a DAO from a database
        // The whole dependency tree is constructed right here, in one place
        val auth = Auth()
        val userDAO = UserServiceAdapter()
        return SignupViewModelFactory(auth, userDAO)
    }

    fun provideMovieDetailViewModelFactory(): MovieDetailViewModelFactory {
        // ViewModelFactory needs a repository, which in turn needs a DAO from a database
        // The whole dependency tree is constructed right here, in one place
        val movieRepository = MovieRepository.getInstance(Database.getInstance().movieDAO)
        val sensorAdmin = SensorAdmin()
        val auth = Auth()
        return MovieDetailViewModelFactory(movieRepository, sensorAdmin, auth)
    }

    // This will be called from Profile
    fun provideProfileViewModelFactory(): ProfileViewModelFactory {
        // ViewModelFactory needs a repository, which in turn needs a DAO from a database
        // The whole dependency tree is constructed right here, in one place
        val userRepository = UserRepository.getInstance(Database.getInstance().userDAO, Database.getInstance().lruChacheUser)
        val genreRepository = GenreRepository.getInstance(Database.getInstance().genreDAO)
        val sensorAdmin = SensorAdmin()
        val auth = Auth()

        return ProfileViewModelFactory(userRepository,genreRepository, sensorAdmin, auth)
    }

    // This will be called from Profile
    fun provideMenuViewModelFactory(): MenuViewModelFactory {
        // ViewModelFactory needs a repository, which in turn needs a DAO from a database
        // The whole dependency tree is constructed right here, in one place
        val auth = Auth()

        return MenuViewModelFactory(auth)
    }
}