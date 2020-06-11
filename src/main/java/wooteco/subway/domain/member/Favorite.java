package wooteco.subway.domain.member;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class Favorite {
    @Id
    private Long id;
    private Long startStationId;
    private Long endStationId;

    private Favorite() {}

    public Favorite(Long startStationId, Long endStationId) {
        this(null, startStationId, endStationId);
    }

    public Favorite(Long id, Long startStationId, Long endStationId) {
        this.id = id;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public static Favorite of(Long startStationId, Long endStationId) {
        return new Favorite(startStationId, endStationId);
    }

    public Long getId() {
        return id;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }

    public boolean isSameId(Long favoriteId) {
        return id.equals(favoriteId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) &&
                Objects.equals(startStationId, favorite.startStationId) &&
                Objects.equals(endStationId, favorite.endStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startStationId, endStationId);
    }
}
