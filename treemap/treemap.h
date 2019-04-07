/*Copyright 2009 Giorgio Zoppi <giorgio.zoppi@gmail.com>
* Licensed under the Apache License, Version 2.0 (the "License"); 
* you may not use this file except in compliance with the License. 
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
* Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
* See the License for the specific language governing permissions and limitations under the License. 
* */
#ifndef TREE_MAP
#define TREE_MAP
#include <ctype.h>
#include <stdlib.h>
#include <pthread.h>

typedef struct t_node
{
  enum
  { RED = 0, BLACK } colour;
  void *key;
  void *value;
  struct t_node *left;
  struct t_node *right;
  struct t_node *parent;
} Node;
typedef struct
{
  Node *rootNode;
  Node *nil;
  pthread_mutex_t big_lock;
  int (*cmp) (void *item1, void *item2);
  Node *(*clone) (void *key, void *item);
  void (*delete) (Node ** item);
} TreeMap;

#define RED_COLOR 0
#define BLACK_COLOR 1

#define IS_EMPTY_ROOT(root)((root)->rootNode == NULL)
#define color(r)   ((r)->colour & 1)
#define is_red(r)   (!color(r))
#define is_black(r) color(r)
#define set_black(r) r->colour = 1;
#define set_red(r)  r->colour = 0;
TreeMap *new_tree_map (Node * (*clone) (void *key, void *data),
		       int (*cmp) (void *k1, void *k2),
		       void (*delete) (Node ** item));
Node *searchKey (TreeMap * t, void *key);
void insertKey (TreeMap * t, void *key, void *value);
Node *updateKey (TreeMap * t, void *key, void *value);
Node *eraseKey (TreeMap * t, void *key);
void destroyKey (TreeMap * t, void *key);
int eraseNode (TreeMap * t, Node * n);
void destroy_tree_map (TreeMap * t);
void replaceKey (TreeMap * t, void *key, void *newKey);
#endif
