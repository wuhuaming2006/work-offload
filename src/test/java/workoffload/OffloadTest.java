package workoffload;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.rules.ExpectedException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public class OffloadTest {
    Offload.Node a, b, c, d, e, f;

    @Before
    public void setUp() {
        a = new Offload.Node(0, 0, false);
        b = new Offload.Node(4, 1);
        c = new Offload.Node(8, 2);
        d = new Offload.Node(8, 2, false);
        e = new Offload.Node(4, 1);
        f = new Offload.Node(8, 2);

        a.addEdge(b, 10);
        b.addEdge(c, 6);
        c.addEdge(d, 5);
        d.addEdge(e, 5);
        e.addEdge(f, 4);
    }

    @Test
    public void testLinear()
        throws Exception {
        Offload offload = new Offload(a, b, c, d, e, f);
        Offload.Result result = offload.optimize(CostModel.responseTime());

        Assert.assertEquals(new HashSet(Arrays.asList(a, b, c, d)), result.local);
        Assert.assertEquals(new HashSet(Arrays.asList(e, f)), result.remote);
    }

    @Test
    public void testResultComparison() {
        Offload.Result dummyResult = new Offload.Result();
        dummyResult.local = new HashSet<Offload.Node>(Arrays.asList(a, b));
        dummyResult.remote = new HashSet(Arrays.asList(c, d, e, f));
        Assert.assertEquals(dummyResult.local, new HashSet(Arrays.asList(a, b)));
        Assert.assertEquals(dummyResult.remote, new HashSet(Arrays.asList(c, d, e, f)));

        dummyResult.remote = new HashSet(Arrays.asList(c, d, f, e));
        Assert.assertEquals(dummyResult.remote, new HashSet(Arrays.asList(c, d, f, e)));
    }

    @Test
    public void testPaperGraph()
        throws Exception {
        a = new Offload.Node(0, 0, false);
        b = new Offload.Node(3, 1);
        c = new Offload.Node(3, 1);
        d = new Offload.Node(6, 2);
        e = new Offload.Node(6, 2);
        f = new Offload.Node(9, 3);

        a.addEdge(b, 10);
        b.addEdge(c, 1);
        b.addEdge(d, 2);
        c.addEdge(d, 1);
        c.addEdge(e, 1);
        d.addEdge(e, 2);
        d.addEdge(f, 1);
        e.addEdge(f, 1);

        Offload offload = new Offload(a, b, c, d, e, f);
        Offload.Result result = offload.optimize(CostModel.responseTime());
        Assert.assertEquals(new HashSet(Arrays.asList(a, b)), result.local);
        Assert.assertEquals(new HashSet(Arrays.asList(c, d, e, f)), result.remote);
    }
}