package pages.navigation;

import lombok.*;

@Getter
public enum Pages {

    BROKER("Find your Yavlena broker | Yavlena","/en/broker");
    private final String name;
    private final String path;

    Pages(final String name, final String path){
        this.name = name;
        this.path = path;
    }
}
