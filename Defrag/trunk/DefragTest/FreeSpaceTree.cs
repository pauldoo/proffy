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
            Defrag.IFreeSpace tree = new Defrag.FreeSpaceTree(array);
            Assert.AreEqual(8, tree.TotalFreeSpace());
            Assert.AreEqual(8, tree.LargestFreeSpan());
        }

        [Test]
        public void AllFull()
        {
            BitArray array = new BitArray(5, true);
            Defrag.IFreeSpace tree = new Defrag.FreeSpaceTree(array);
            Assert.AreEqual(0, tree.TotalFreeSpace());
            Assert.AreEqual(0, tree.LargestFreeSpan());
        }

        [Test]
        public void Mixed()
        {
            BitArray array = new BitArray(new Boolean[] { true, true, false, false, true, false, false, false, true, true, false });
            Defrag.IFreeSpace tree = new Defrag.FreeSpaceTree(array);
            Assert.AreEqual(6, tree.TotalFreeSpace());
            Assert.AreEqual(3, tree.LargestFreeSpan());
            Assert.AreEqual(2, tree.FindFreeSpan(1));
            Assert.AreEqual(2, tree.FindFreeSpan(2));
            Assert.AreEqual(5, tree.FindFreeSpan(3));
            Assert.AreEqual(UInt64.MaxValue, tree.FindFreeSpan(4));
        }

        [Test]
        public void AllocateFromEmpty()
        {
            BitArray array = new BitArray(12, false);
            Defrag.IFreeSpace tree = new Defrag.FreeSpaceTree(array);
            Assert.AreEqual(12, tree.TotalFreeSpace());
            Assert.AreEqual(12, tree.LargestFreeSpan());
            tree.Allocate(2, 1);
            Assert.AreEqual(11, tree.TotalFreeSpace());
            Assert.AreEqual(9, tree.LargestFreeSpan());
            Assert.AreEqual(0, tree.FindFreeSpan(1));
            Assert.AreEqual(0, tree.FindFreeSpan(2));
            Assert.AreEqual(3, tree.FindFreeSpan(3));
            Assert.AreEqual(3, tree.FindFreeSpan(4));
            Assert.AreEqual(3, tree.FindFreeSpan(5));
            Assert.AreEqual(3, tree.FindFreeSpan(6));
            Assert.AreEqual(3, tree.FindFreeSpan(7));
            Assert.AreEqual(3, tree.FindFreeSpan(8));
            Assert.AreEqual(3, tree.FindFreeSpan(9));
            Assert.AreEqual(UInt64.MaxValue, tree.FindFreeSpan(10));

            tree.Allocate(6, 2);
            Assert.AreEqual(9, tree.TotalFreeSpace());
            Assert.AreEqual(4, tree.LargestFreeSpan());
            Assert.AreEqual(0, tree.FindFreeSpan(1));
            Assert.AreEqual(0, tree.FindFreeSpan(2));
            Assert.AreEqual(3, tree.FindFreeSpan(3));
            Assert.AreEqual(8, tree.FindFreeSpan(4));
            Assert.AreEqual(UInt64.MaxValue, tree.FindFreeSpan(5));
        }
    }
}
