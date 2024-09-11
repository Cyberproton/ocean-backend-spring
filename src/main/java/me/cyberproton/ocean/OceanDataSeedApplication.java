package me.cyberproton.ocean;

import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.artist.ArtistEntity;
import me.cyberproton.ocean.features.copyright.CopyrightEntity;
import me.cyberproton.ocean.features.genre.GenreEntity;
import me.cyberproton.ocean.features.history.entity.HistoryEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.recordlabel.RecordLabelEntity;
import me.cyberproton.ocean.features.role.Role;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.seed.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.function.Supplier;

@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class})
@ConfigurationPropertiesScan
public class OceanDataSeedApplication {
    public static void main(String[] args) {
        ApplicationContext context =
                new SpringApplicationBuilder(OceanDataSeedApplication.class)
                        .web(WebApplicationType.NONE)
                        .run(args);
        runAndMeasureTime(() -> context.getBean(TableResetter.class).reset(), "Resetting tables");
        runAndMeasureTime(
                () -> context.getBean(ElasticsearchIndicesResetter.class).reset(),
                "Resetting elasticsearch indices");
        runAndMeasureTime(
                () -> context.getBean(StorageResetter.class).reset(), "Resetting storage");
        List<UserEntity> users =
                runAndMeasureTime(() -> context.getBean(UserSeeder.class).seed(), "Seeding users");
        runAndMeasureTime(
                () -> context.getBean(UserFollowerSeeder.class).seed(users),
                "Seeding user followers");
        List<Role> roles =
                runAndMeasureTime(() -> context.getBean(RoleSeeder.class).seed(), "Seeding roles");
        runAndMeasureTime(
                () -> context.getBean(UserRoleSeeder.class).seed(users, roles),
                "Seeding user roles");
        List<ArtistEntity> artists =
                runAndMeasureTime(
                        () -> context.getBean(ArtistSeeder.class).seed(users), "Seeding artists");
        runAndMeasureTime(
                () -> context.getBean(ProfileSeeder.class).seed(users), "Seeding profiles");
        List<RecordLabelEntity> recordLabels =
                runAndMeasureTime(
                        () -> context.getBean(RecordLabelSeeder.class).seed(),
                        "Seeding record labels");
        List<CopyrightEntity> copyrights =
                runAndMeasureTime(
                        () -> context.getBean(CopyrightSeeder.class).seed(), "Seeding copyrights");
        List<AlbumEntity> albums =
                runAndMeasureTime(
                        () ->
                                context.getBean(AlbumSeeder.class)
                                        .seed(users, copyrights, recordLabels),
                        "Seeding albums");
        List<GenreEntity> genres =
                runAndMeasureTime(
                        () -> context.getBean(GenreSeeder.class).seed(), "Seeding genres");
        List<PlaylistEntity> playlists =
                runAndMeasureTime(
                        () -> context.getBean(PlaylistSeeder.class).seed(users),
                        "Seeding playlists");
        List<TrackEntity> tracks =
                runAndMeasureTime(
                        () -> context.getBean(TrackSeeder.class).seed(), "Seeding tracks");
        runAndMeasureTime(
                () -> context.getBean(PlaylistTrackSeeder.class).seed(), "Seeding playlist tracks");
        List<HistoryEntity> histories =
                runAndMeasureTime(
                        () ->
                                context.getBean(HistorySeeder.class)
                                        .seed(users, artists, albums, playlists, tracks),
                        "Seeding histories");
        SpringApplication.exit(context);
    }

    public static <T> T runAndMeasureTime(Supplier<T> task, String message) {
        System.out.println(message);
        long startTime = System.currentTimeMillis();
        T res = task.get();
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
        return res;
    }

    public static void runAndMeasureTime(Runnable task, String message) {
        System.out.println(message);
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
