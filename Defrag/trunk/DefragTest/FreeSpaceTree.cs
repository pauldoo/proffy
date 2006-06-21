using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using Defrag;
using NUnit.Framework;

namespace DefragTest
{
    [TestFixture]
    public class FreeSpaceTree
    {
        [Test]
        public void AllEmpty()
        {
            BitArray array = new BitArray(8, false);
            Defrag.FreeSpaceTree tree = new Defrag.FreeSpaceTree(array);
            Assert.AreEqual(8, tree.TotalFreeSpace());
            Assert.AreEqual(8, tree.LargestFreeSpan());
        }

        [Test]
        public void AllFull()
        {
            BitArray array = new BitArray(5, true);
            Defrag.FreeSpaceTree tree = new Defrag.FreeSpaceTree(array);
            Assert.AreEqual(0, tree.TotalFreeSpace());
            Assert.AreEqual(0, tree.LargestFreeSpan());
        }

        [Test]
        public void Mixed()
        {
            BitArray array = new BitArray(new Boolean[] { true, true, false, false, true, false, false, false, true, true, false });
            Defrag.FreeSpaceTree tree = new Defrag.FreeSpaceTree(array);
            Assert.AreEqual(6, tree.TotalFreeSpace());
            Assert.AreEqual(3, tree.LargestFreeSpan());
            Assert.AreEqual(2, tree.FindFreeSpan(1));
            Assert.AreEqual(2, tree.FindFreeSpan(2));
            Assert.AreEqual(5, tree.FindFreeSpan(3));
            Assert.AreEqual(UInt64.MaxValue, tree.FindFreeSpan(4));
        }
    }
}
