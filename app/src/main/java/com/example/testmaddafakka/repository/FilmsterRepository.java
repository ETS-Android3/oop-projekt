package com.example.testmaddafakka.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.testmaddafakka.api.ApiListener;
import com.example.testmaddafakka.api.IAdapter;
import com.example.testmaddafakka.api.IMDbApiAdapter;
import com.example.testmaddafakka.model.Filmster;
import com.example.testmaddafakka.model.IPreferences;
import com.example.testmaddafakka.model.IMedia;
import com.example.testmaddafakka.api.IApiListener;
import com.example.testmaddafakka.model.MovieStatusItem;
import com.example.testmaddafakka.model.Preferences;
import com.example.testmaddafakka.model.User;
import com.example.testmaddafakka.model.WatchList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * A repository that handles all communication between the viewmodels and the api/model data
 * Follows the mvvm architecture
 */
public class FilmsterRepository implements IApiListener {

    private static FilmsterRepository instance;
    private IAdapter imdbAdapter;
    private MutableLiveData<List<IMedia>> medias;
    private MutableLiveData<IMedia> currentMedia;
    private ApiListener listener;
    private Filmster filmster;
    private User user;
    private int current = 0;
    private Preferences preferences;
    private MutableLiveData<List<IPreferences>> categories;

    FirebaseAuth mauth= FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    private FilmsterRepository(Context ctx) {

        medias = new MutableLiveData<>();
        currentMedia = new MutableLiveData<>();
        user = new User("TestNamn", "TestPass", new WatchList(), new Preferences());
        filmster = new Filmster(user);
        listener = new ApiListener();
        listener.addListener(this);
        categories = new MutableLiveData<List<IPreferences>>();

        imdbAdapter = new IMDbApiAdapter(ctx, listener);
        loadMedias();
        setDataFromFirebase();
    }

    public static FilmsterRepository getInstance(@Nullable Context ctx) {
        if (instance == null) {
            instance = new FilmsterRepository(ctx);
        }
        return instance;
    }

    public void loadMedias() {
        imdbAdapter.get250Movies();
    }

    public MutableLiveData<IMedia> getCurrentMedia() {
        this.currentMedia.setValue(filmster.getCurrentMedia());
        return this.currentMedia;
    }

    private void setMedias(List<IMedia> medias) {
        this.medias.setValue(medias);
        filmster.setMediaList(medias);
    }

    @Override
    public void update(List<IMedia> medias) {
        setMedias(medias);
    }

    public void addLikedMedia(IMedia media) {
        filmster.addLikedMedia(media);
        DatabaseReference myRef = database.getReference("moviesandusers");
        MovieStatusItem item = new MovieStatusItem(media.getId(),mauth.getCurrentUser().getUid(),"Liked");
        myRef.push().setValue(item);

        nextMedia();
    }

    public void addDislikedMedia(IMedia media) {
        filmster.addDislikedMedia(media);


        DatabaseReference myRef = database.getReference("moviesandusers");
        MovieStatusItem item = new MovieStatusItem(media.getId(),mauth.getCurrentUser().getUid(),"Disliked");
        myRef.push().setValue(item);
        nextMedia();
    }
    public void addWatchedMedia(IMedia media){
        filmster.addWatchedMedia(media);
        DatabaseReference myRef = database.getReference("moviesandusers");
        MovieStatusItem item = new MovieStatusItem(media.getId(),mauth.getCurrentUser().getUid(),"Watched");
        myRef.push().setValue(item);
        nextMedia();
    }

    public void nextMedia() {
        //listener.notifyListeners(this.movies.getValue().get(current));
        //ERRRRORORORORORROR
        this.currentMedia.setValue(this.medias.getValue().get(current));
        current++;
    }
    public List<IMedia> getLikedMedias(){
        return user.getLikedMedia();
    }
    public List<IMedia> getDislikedMedias(){
        return user.getDislikedMedia();
    }
    public List<IMedia> getWatchedMedias(){
        return user.getWatchedMedia();
    }


    public void loadSelectedCategory(String categoryName){
        imdbAdapter.getList(getSelectedCategory(categoryName));
    }

    public String getSelectedCategory(String categoryName){
        return filmster.CurrentUsersCategory(categoryName);
    }

    public MutableLiveData<List<IPreferences>> getCategories() {
        this.categories.setValue(filmster.getMovieCategories());
        return this.categories;
    }

    public void setDataFromFirebase(){
        database = FirebaseDatabase.getInstance();
        Query query = database.getReference("moviesandusers").orderByChild("userId").equalTo(mauth.getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        MovieStatusItem msi = new MovieStatusItem(ds.child("movieId").getValue(String.class),ds.child("userId").getValue(String.class),ds.child("status").getValue(String.class));

                        if(msi.getStatus().equals("Liked")){
                            Log.i("test",msi.getMovieId());
                        }else if(msi.getStatus().equals("Disliked")){
                            Log.i("test",msi.getMovieId());
                        }else{
                            Log.i("test",msi.getMovieId());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
