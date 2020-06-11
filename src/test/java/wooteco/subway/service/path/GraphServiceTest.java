package wooteco.subway.service.path;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GraphServiceTest {
    private static final String STATION_NAME_KANGNAM = "강남역";
    private static final String STATION_NAME_YEOKSAM = "역삼역";
    private static final String STATION_NAME_SEOLLEUNG = "선릉역";
    private static final String STATION_NAME_YANGJAE = "양재역";
    private static final String STATION_NAME_YANGJAE_CITIZENS_FOREST = "양재시민의숲역";
    private static final String STATION_NAME_SEOUL = "서울역";

    private GraphService graphService;

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;

    private Line line1;
    private Line line2;

    @BeforeEach
    void setUp() {
        graphService = new GraphService();

        station1 = new Station(1L, STATION_NAME_KANGNAM);
        station2 = new Station(2L, STATION_NAME_YEOKSAM);
        station3 = new Station(3L, STATION_NAME_SEOLLEUNG);
        station4 = new Station(4L, STATION_NAME_YANGJAE);
        station5 = new Station(5L, STATION_NAME_YANGJAE_CITIZENS_FOREST);
        station6 = new Station(6L, STATION_NAME_SEOUL);

        line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 1L, 10, 10));
        line1.addLineStation(new LineStation(1L, 2L, 10, 10));
        line1.addLineStation(new LineStation(2L, 3L, 10, 10));

        line2 = new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 1L, 10, 10));
        line2.addLineStation(new LineStation(1L, 4L, 10, 10));
        line2.addLineStation(new LineStation(4L, 5L, 10, 10));
    }

    @Test
    void findPath() {
        List<Long> stationIds = graphService.findPath(Lists.list(line1, line2), station3.getId(), station5.getId(), PathType.DISTANCE);

        assertThat(stationIds).hasSize(5);
        assertThat(stationIds.get(0)).isEqualTo(3L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(1L);
        assertThat(stationIds.get(3)).isEqualTo(4L);
        assertThat(stationIds.get(4)).isEqualTo(5L);
    }

    @Test
    void findPathWithNoPath() {
        assertThrows(IllegalArgumentException.class, () ->
                graphService.findPath(Lists.list(line1, line2), station3.getId(), station6.getId(), PathType.DISTANCE)
        );

    }

    @Test
    void findPathWithDisconnected() {
        line2.removeLineStationById(station1.getId());

        assertThrows(IllegalArgumentException.class, () ->
                graphService.findPath(Lists.list(line1, line2), station3.getId(), station6.getId(), PathType.DISTANCE)
        );
    }
}
