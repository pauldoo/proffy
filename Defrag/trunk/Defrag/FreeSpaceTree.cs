using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;

namespace Defrag
{
    public class FreeSpaceTree : IFreeSpace
    {
        public FreeSpaceTree(BitArray map)
        {
            List<IFreeSpace> nodes = new List<IFreeSpace>();
            ulong free_space_span = 0;
            for (uint i = 0; i <= (uint)map.Length; i++)
            {
                if (i == (uint)map.Length || map.Get((int)i))
                {
                    if (free_space_span > 0)
                    {
                        nodes.Add(new Leaf(i - free_space_span, free_space_span));
                    }
                    free_space_span = 0;
                }
                else
                {
                    free_space_span++;
                }
            }

            while (nodes.Count > 1)
            {
                List<IFreeSpace> new_nodes = new List<IFreeSpace>();
                List<IFreeSpace>.Enumerator enumerator = nodes.GetEnumerator();
                while (enumerator.MoveNext())
                {
                    IFreeSpace left = enumerator.Current;
                    if (enumerator.MoveNext())
                    {
                        IFreeSpace right = enumerator.Current;
                        new_nodes.Add(new Node(left, right));
                    }
                    else
                    {
                        new_nodes.Add(left);
                    }
                }
                nodes = new_nodes;
            }
            if (nodes.Count > 0)
            {
                top_node = nodes[0];
            }
        }

        public ulong TotalFreeSpace()
        {
            return (top_node != null) ? top_node.TotalFreeSpace() : 0;
        }

        public ulong LargestFreeSpan()
        {
            return (top_node != null) ? top_node.LargestFreeSpan() : 0;
        }

        public ulong FindFreeSpan(ulong length)
        {
            return (top_node != null) ? top_node.FindFreeSpan(length) : UInt64.MaxValue;
        }

        private IFreeSpace top_node;
    }

    class Node : IFreeSpace
    {
        public Node(IFreeSpace left, IFreeSpace right)
        {
            this.left_node = left;
            this.right_node = right;
            largest_free_span = Math.Max(
                (left_node != null) ? left_node.LargestFreeSpan() : 0,
                (right_node != null) ? right_node.LargestFreeSpan() : 0);
        }

        public ulong TotalFreeSpace()
        {
            return
                ((left_node != null) ? left_node.TotalFreeSpace() : 0) +
                ((right_node != null) ? right_node.TotalFreeSpace() : 0);
        }

        public ulong LargestFreeSpan()
        {
            return largest_free_span;
        }

        public ulong FindFreeSpan(ulong length)
        {
            if (largest_free_span < length)
            {
                return UInt64.MaxValue;
            }
            else
            {
                if (left_node.LargestFreeSpan() >= length)
                {
                    return left_node.FindFreeSpan(length);
                }
                else
                {
                    return right_node.FindFreeSpan(length);
                }
            }

        }

        private IFreeSpace left_node;
        private IFreeSpace right_node;
        private ulong largest_free_span;
    }

    class Leaf : IFreeSpace
    {
        public Leaf(ulong offset, ulong length) {
            this.offset = offset;
            this.length = length;
        }

        public ulong TotalFreeSpace()
        {
            return length;
        }

        public ulong LargestFreeSpan()
        {
            return length;
        }

        public ulong FindFreeSpan(ulong length)
        {
            if (this.length >= length)
            {
                return offset;
            }
            else
            {
                return UInt64.MaxValue;
            }
        }

        private ulong offset;
        private ulong length;
    }
}
