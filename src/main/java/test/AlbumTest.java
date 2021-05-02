package test;

import musichub.business.Album;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

class AlbumTest {

    @Test
    @DisplayName("test1")
    public void test() {
        Album a =new Album("aa","aaa",11,"1092-10-10");
        System.out.println(a.getTitle());
    }
}