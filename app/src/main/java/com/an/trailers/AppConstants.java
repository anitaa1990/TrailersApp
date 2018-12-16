package com.an.trailers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface AppConstants {

    int PAGE_LIMIT = 10;


    String CREDIT_CAST = "cast";
    String CREDIT_CREW = "crew";

    String INTENT_MOVIE = "movie";
    String INTENT_CATEGORY = "category";
    String INTENT_VIDEO_KEY = "intent_video_key";

    String TRANSITION_IMAGE_NAME = "image";

    String TYPE_MOVIES = "movie";
    String TYPE_TVS = "tv";
    String MOVIES_POPULAR = "popular";
    String MOVIES_UPCOMING = "upcoming";
    String MOVIES_TOP_RATED = "top_rated";
    String TV_ON_THE_AIR = "on_the_air";

    String MOVIE_STATUS_RELEASED = "Released";

    String BASE_URL = "https://api.themoviedb.org/3/";
    String IMAGE_URL = "https://image.tmdb.org/t/p/w500%s";

    String TMDB_API_KEY = "5e74ee79280d770dc8ed5a2fbdda955a";
    String YOUTUBE_API_KEY = "AIzaSyCZY8Vnw_6GcJcESL-NilTZDMSvg9ViLt8";

    Map<Integer, String> MENU_MOVIE_ITEM = Collections.unmodifiableMap(
            new HashMap<Integer, String>() {{
                put(0, MOVIES_POPULAR);
                put(1, MOVIES_UPCOMING);
                put(2, MOVIES_TOP_RATED);
            }});

    Map<Integer, String> MENU_TV_ITEM = Collections.unmodifiableMap(
            new HashMap<Integer, String>() {{
                put(0, MOVIES_POPULAR);
                put(1, TV_ON_THE_AIR);
                put(2, MOVIES_TOP_RATED);
            }});
}
