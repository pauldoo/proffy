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
                top_node00 = nodes[0];
            }
        }

        public ulong TotalFreeSpace()
        {
            return (top_node00 != null) ? top_node00.TotalFreeSpace() : 0;
        }

        public ulong LargestFreeSpan()
        {
            return (top_node00 != null) ? top_node00.LargestFreeSpan() : 0;
        }

        public ulong FindFreeSpan(ulong length)
        {
            return (top_node00 != null) ? top_node00.FindFreeSpan(length) : UInt64.MaxValue;
        }

        public IFreeSpace Allocate(ulong offset, ulong length)
        {
            if (top_node00 != null)
            {
                top_node00 = top_node00.Allocate(offset, length);
            }
            return this;
        }

        private IFreeSpace top_node00;
    }

    class Node : IFreeSpace
    {
        public Node(IFreeSpace left, IFreeSpace right)
        {
            this.left_node = left;
            this.right_node = right;
            largest_free_span = Math.Max(
                left_node.LargestFreeSpan(),
                right_node.LargestFreeSpan());
        }

        public ulong TotalFreeSpace()
        {
            return
                left_node.TotalFreeSpace() +
                right_node.TotalFreeSpace();
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

        public IFreeSpace Allocate(ulong offset, ulong length)
        {
            if (left_node.FindFreeSpan(1) < offset + length)
            {
                ulong right_node_offset = right_node.FindFreeSpan(1);
                if (right_node_offset > offset)
                {
                    left_node = left_node.Allocate(offset, length);
                }
                if (right_node_offset < offset + length)
                {
                    right_node = right_node.Allocate(offset, length);
                }
                if (left_node == null && right_node == null)
                {
                    return null;
                }
                if (left_node == null)
                {
                    return right_node;
                }
                if (right_node == null)
                {
                    return right_node;
                }
                largest_free_span = Math.Max(
                    left_node.LargestFreeSpan(),
                    right_node.LargestFreeSpan());
            }
            return this;
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

        public IFreeSpace Allocate(ulong offset, ulong length)
        {
            ulong left_offset = Math.Min(offset, this.offset);
            ulong left_length = (offset > left_offset) ? (offset - left_offset) : 0;
            ulong right_offset = Math.Min(offset + length, this.offset + this.length);
            ulong right_length = (right_offset > this.offset + this.length) ? 0 : (this.offset + this.length - right_offset);
            
            if (left_length > 0 && right_length > 0)
            {
                return new Node(new Leaf(left_offset, left_length), new Leaf(right_offset, right_length));
            }
            else if (left_length > 0)
            {
                this.offset = left_offset;
                this.length = left_length;
                return this;
            }
            else if (right_length > 0)
            {
                this.offset = right_offset;
                this.length = right_length;
                return this;
            }
            else
            {
                return null;
            }
        }

        private ulong offset;
        private ulong length;
    }
}
