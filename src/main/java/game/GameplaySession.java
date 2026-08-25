package game;

// this class is responsible for tracking the state of a gameplay session,
// including the beatmap, score, and hit judgements
public class GameplaySession {
    private final Beatmap beatmap;
    private final ScoreTracker scoreTracker = new ScoreTracker();
    private HitJudgement latestJudgement;
    private int resolvedNotes;

    public GameplaySession(Beatmap beatmap) {
        this.beatmap = beatmap;
    }

    public HitJudgement hit(Lane lane, long songTimeMs) {
        Note nearestNote = null;
        long nearestDifference = Long.MAX_VALUE;

        for (Note note : beatmap.notes()) {
            if (!note.isPending() || note.getLane() != lane) {
                continue;
            }

            long difference = Math.abs(songTimeMs - note.getHitTimeMs());
            if (difference <= HitJudgement.MAX_HIT_WINDOW_MS && difference < nearestDifference) {
                nearestNote = note;
                nearestDifference = difference;
            }
        }

        if (nearestNote == null) {
            return null;
        }

        nearestNote.markHit();
        resolvedNotes++;
        latestJudgement = HitJudgement.fromTimingDifference(nearestDifference);
        scoreTracker.register(latestJudgement);
        return latestJudgement;
    }

    public void update(long songTimeMs) {
        for (Note note : beatmap.notes()) {
            if (note.isPending()
                    && songTimeMs - note.getHitTimeMs() > HitJudgement.MAX_HIT_WINDOW_MS) {
                markMissed(note);
            }
        }
    }

    public void missAllPendingNotes() {
        for (Note note : beatmap.notes()) {
            if (note.isPending()) {
                markMissed(note);
            }
        }
    }

    private void markMissed(Note note) {
        note.markMissed();
        resolvedNotes++;
        latestJudgement = HitJudgement.MISS;
        scoreTracker.register(HitJudgement.MISS);
    }

    public boolean isComplete() {
        return resolvedNotes == beatmap.notes().size();
    }

    public ScoreTracker getScoreTracker() {
        return scoreTracker;
    }

    public HitJudgement getLatestJudgement() {
        return latestJudgement;
    }
}
