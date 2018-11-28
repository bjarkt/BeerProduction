package org.grp2.unitTest;

import org.grp2.domain.OEE;
import org.grp2.shared.Batch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OEETest {

    @Test
    public void testOEE() {
        List[] data = setupOEETestData();
        List<Batch> batches = data[0];
        List<Double> stopTimes = data[1];
        List<Double> expectedOEE = data[2];

        for (int i = 0; i < batches.size(); i++) {
            Batch batch = batches.get(i);
            Double stopTime = stopTimes.get(i);
            OEE oee = new OEE(batch, stopTime);

            assertEquals(expectedOEE.get(i), oee.getOEE(), 0);
        }
    }

    private List[] setupOEETestData() {
        List<Batch> batches = new ArrayList<>();
        List<Double> stopTimes = new ArrayList<>();
        List<Double> oees = new ArrayList<>();

        batches.add(new Batch("testBeerName", -1, -1, null, null, 100, 0, 300));
        batches.add(new Batch("testBeerName", -1, -1, null, null, 100, 0, 60));

        Collections.addAll(stopTimes, 117.0, 4.0);

        Collections.addAll(oees, -4.85, 0.96);

        return new List[]{batches, stopTimes, oees};
    }
}
