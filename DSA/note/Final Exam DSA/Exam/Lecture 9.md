# 1. Question 1
What is the order of growth of the expected height of a binary search tree with n keys after a long intermixed sequence of random insertions and random Hibbard deletions?

- The main defect of Hibbard deletion is that it unbalances the tree, leading to $\sqrt(n)$​ height. 
- If instead of replacing the node to delete with its successor, you flip a coin and replace it with either its successor or predecessor, then, in practice, the height becomes logarithmic (but nobody has been able to prove this fact mathematically).

# Question 2

**Check if a binary tree is a BST.** Given a binary tree where each NodeNode contains a key, determine whether it is a binary search tree. Use extra space proportional to the height of the tree.

design a recursive function isBST(Nodex,Keymin,Keymax) that determines whether x is the root of a binary search tree with all keys between min and max.

# Question 3

**Inorder traversal with constant extra space**. Design an algorithm to perform an inorder traversal of a binary search tree using only a constant amount of extra space.

Morris Traversal: create a temporary link to make a sequence of inorder node
- If no left child, visit this node and go to right
- Find the inorder predecessor of current
- If curr the right child of its inorder predecessor
- Revert the change made in tree structure when it return it right in 
Pseudocode:
- Initialize ***current as root***
- While current is ***not NULL***
- If the current does not have left child
    - ***Print*** current’s data
    - Go to the right, i.e., **current = current->right***
- Else, find rightmost node in current left subtree OR node whose ***right child == current.***
- If we found ***right child == current*** // revert the change
    - ***Update*** the right child as NULL of that node whose right child is current
    - ***Print*** current’s data
    - Go to the right, i.e. ***current = current->right***
- Else // make a temporary
    - Make current as the ***right child*** of that rightmost node we found; and
    - Go to this left child, i.e., ***current = current->left***
# Question 4

**Web tracking.** Suppose that you are tracking nn web sites and mm users and you want to support the following API:

- User visits a website.
    
- How many times has a given user visited a given site?
    

What data structure or data structures would you use?

_Hint_: maintain a symbol table of symbol tables.
- Symbol table : the key is userID, value is another symbol table
- Nested symbol table: key is website id, value is the count of visits of given user which is the key of outer symbol table.

# Question 5

**Document search.** Design an algorithm that takes a sequence of n document words and a sequence of m query words and find the shortest interval in which the m query words appear in the document in the order given. The length of an interval is the number of words in that interval.

_Hint_: for each word, maintain a sorted list of the indices in the document in which that word appears. Scan through the sorted lists of the query words in a judicious manner.

# Question 6

**Generalized queue**. Design a generalized queue data type that supports all of the following operations in logarithmic time (or better) in the worst case.

- Create an empty data structure.
    
- Append an item to the end of the queue.
    
- Remove an item from the front of the queue.
    
- Return the $i^{th}$ item in the queue.
    
- Remove the $i^{th}$ item from the queue.

Hint: create a red–black BST where the keys are integers and the values are the items such that the $i^{th}$ largest integer key in the red–black BST corresponds to the $i^{th}$ item in the queue.
- Generate key from one to some limit, if exceed limit reconstruct red-black BST
- Return the $i^{th}$ item from the queue by select($i^{th}$ - rank). For ex, $i^{th}$ element is node with rank 0 aka no more smaller key, because key in this tree represent indices.