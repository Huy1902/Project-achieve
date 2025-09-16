  
# Question 1

**Stack with max.** Create a data structure that efficiently supports the stack operations (push and pop) and also a return-the-maximum operation. Assume the elements are real numbers so that you can compare them

_Hint:_ Use two stacks, one to store all of the items and a second stack to store the maximums.
When push into all element stack, if that larger than top of maximum stack, we push it into maximum stack to then it become new max

# Question 2

**Java generics.** Explain why Java prohibits generic array creation

_Hint:_ to start, you need to understand that Java arrays are _covariant_ but Java generics are not: that is, String is a subtype of Object, but Stack<String> is not a subtype of Stack<Object>.