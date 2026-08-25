package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HitJudgementTest {
    @Test
    void mapsTimingWindowBoundariesToJudgements() {
        assertEquals(HitJudgement.PERFECT_300, HitJudgement.fromTimingDifference(45));
        assertEquals(HitJudgement.GREAT_200, HitJudgement.fromTimingDifference(46));
        assertEquals(HitJudgement.GREAT_200, HitJudgement.fromTimingDifference(-90));
        assertEquals(HitJudgement.GOOD_100, HitJudgement.fromTimingDifference(91));
        assertEquals(HitJudgement.OK_50, HitJudgement.fromTimingDifference(180));
        assertEquals(HitJudgement.MISS, HitJudgement.fromTimingDifference(181));
    }
}
