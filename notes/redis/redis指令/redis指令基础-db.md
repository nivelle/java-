### DB

#### 1. exists

- 返回值: 若 key 存在，返回 1 ，否则返回 0 。
     
- 检查给定 key 是否存在。

#### 2. TYPE key

- 返回 key 所储存的值的类型。

#### 3. rename key newkey

- 返回值: 改名成功时提示 OK ，失败时候返回一个错误。
     
- 将 key 改名为 newkey 。
  
- 当 key 和 newkey 相同，或者 key 不存在时，返回一个错误。
   
- 当 newkey 已经存在时， RENAME 命令将覆盖旧值。

#### 4. renamenx key newkey

- 返回值:修改成功时，返回1;如果newkey已经存在，返回0;当且仅当 newkey 不存在时，将 key 改名为 newkey 。
  
- 当key不存在时,返回一个错误

#### 5. MOVE key db

- 返回值:移动成功返回 1,失败则返回 0 。
    
- 将当前数据库的 key 移动到给定的数据库 db 当中。
  
- 如果当前数据库(源数据库)和给定数据库(目标数据库)有相同名字的给定 key,或者 key 不存在于当前数据库，那么 MOVE 没有任何效果。

#### 6. del key [key …]

- 返回值： 被删除key的数量

- 时间复杂度：O(N)， N 为被删除的 key 的数量，其中删除单个字符串类型的 key ，时间复杂度为O(1)；删除单个列表、集合、有序集合或哈希表类型的 key ，时间复杂度为O(M)， M 为以上数据结构内的元素数量。

- 删除给定的一个或多个 key;不存在的 key 会被忽略。


#### 7. randomkey

- 返回值: 当数据库不为空时，返回一个 key 。 当数据库为空时，返回 nil 。
     
- 从当前数据库中随机返回(不删除)一个 key 。
   

#### 8. dbsize

- 返回值：当前数据的key的数量

- 返回当前数据库的 key 的数量。
  

#### 9. KEYS pattern

- 返回值:符合给定模式的 key 列表。
    
- 查找所有符合给定模式 pattern 的 key 

````

- KEYS * 匹配数据库中所有 key

- KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。

- KEYS h*llo 匹配 hllo 和 heeeeello 等。

- KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。

````
#### 10. flushdb

- 总是返回OK

- 清空当前数据库中的所有 key。


#### 11. flushall

- 清空整个 Redis 服务器的数据(删除所有数据库的所有 key )。
  
- 此命令从不失败。

#### 12. select

- 切换到指定的数据库，数据库索引号 index 用数字值指定，以 0 作为起始索引值。
  
- 默认使用0号数据库


