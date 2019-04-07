/*Copyright 2009 Giorgio Zoppi <giorgio.zoppi@gmail.com>
* Licensed under the Apache License, Version 2.0 (the "License"); 
* you may not use this file except in compliance with the License. 
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
* Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
* See the License for the specific language governing permissions and limitations under the License. 
* */
#include <treemap.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>

TreeMap *
new_tree_map (Node * (*clone) (void *key, void *data),
	      int (*cmp) (void *key1, void *key2),
	      void (*delete) (Node ** item))
{
  TreeMap *t = calloc (1, sizeof (TreeMap));
  t->nil = calloc (1, sizeof (Node));
  t->nil->left = t->nil->right = t->nil->parent = t->nil;
  t->nil->colour = BLACK;
  t->nil->key = NULL;
  t->nil->value = NULL;
  t->rootNode = calloc (1, sizeof (Node));	/*allocate sentinel */
  t->rootNode->left = t->rootNode->right = t->rootNode->parent = t->nil;
  t->rootNode->key = NULL;
  t->rootNode->value = NULL;
  t->rootNode->colour = BLACK;	/*sentinel is black */
  t->clone = clone;
  t->cmp = cmp;
  t->delete = delete;
  pthread_mutex_init (&t->big_lock, NULL);
  return t;
}

static void
insertNode (TreeMap * t, Node * n)
{
  Node *x, *y;

  n->left = n->right = t->nil;

  y = t->rootNode;
  x = t->rootNode->left;
  while (x != t->nil)
    {
      y = x;
      if ((t->cmp (x->key, n->key) == 1))
	x = x->left;
      else
	x = x->right;
    }
  n->parent = y;
  if ((y == t->rootNode) || (t->cmp (y->key, n->key) == 1))
    {
      y->left = n;
    }
  else
    {
      y->right = n;
    }
}

static void
freeTree (TreeMap * t, Node * current)
{

  if ((current != t->nil))
    {
      freeTree (t, current->left);
      freeTree (t, current->right);
      t->delete (&current);
      free (current);
    }
}

static Node *
search (TreeMap * t, Node * current, void *keyval)
{
  if (current != t->nil)
    {
      if (t->cmp (current->key, keyval) > 0)
	return search (t, current->left, keyval);
      else if (t->cmp (current->key, keyval) < 0)
	return search (t, current->right, keyval);
      else if ((t->cmp (current->key, keyval) == 0))
	return current;
    }
  return current;
}

static void
LeftRotate (TreeMap * t, Node * x)
{

  Node *y;

  y = x->right;
  x->right = y->left;

  if (y->left != t->nil)
    y->left->parent = x;

  y->parent = x->parent;


  if (x == x->parent->left)
    {
      x->parent->left = y;
    }
  else
    {
      x->parent->right = y;
    }
  y->left = x;
  x->parent = y;

}

static void
RightRotate (TreeMap * t, Node * y)
{
  Node *x;

  x = y->left;
  y->left = x->right;

  if (x->right != t->nil)
    x->right->parent = y;

  x->parent = y->parent;
  if (y == y->parent->left)
    {
      y->parent->left = x;
    }
  else
    {
      y->parent->right = x;
    }
  x->right = y;
  y->parent = x;
}

static void
balanceAfterDeletion (TreeMap * tree, Node * x)
{
  Node *root = tree->rootNode->left;
  Node *w;

  while ((x->colour == BLACK) && (root != x))
    {
      if (x == x->parent->left)
	{
	  w = x->parent->right;
	  if (w->colour == RED)
	    {
	      w->colour = BLACK;
	      x->parent->colour = RED;
	      LeftRotate (tree, x->parent);
	      w = x->parent->right;
	    }
	  if ((w->right->colour == BLACK) && (w->left->colour == BLACK))
	    {
	      w->colour = RED;
	      x = x->parent;
	    }
	  else
	    {
	      if (w->right->colour == BLACK)
		{
		  w->left->colour = BLACK;
		  w->colour = RED;
		  RightRotate (tree, w);
		  w = x->parent->right;
		}
	      w->colour = x->parent->colour;
	      x->parent->colour = BLACK;
	      w->right->colour = BLACK;
	      LeftRotate (tree, x->parent);
	      x = root;		/* this is to exit while loop */
	    }
	}
      else
	{
	  w = x->parent->left;
	  if (w->colour == RED)
	    {
	      w->colour = BLACK;
	      x->parent->colour = RED;
	      RightRotate (tree, x->parent);
	      w = x->parent->left;
	    }
	  if ((w->right->colour == BLACK) && (w->left->colour == BLACK))
	    {
	      w->colour = RED;
	      x = x->parent;
	    }
	  else
	    {
	      if (w->left->colour == BLACK)
		{
		  w->right->colour = BLACK;
		  w->colour = RED;
		  LeftRotate (tree, w);
		  w = x->parent->left;
		}
	      w->colour = x->parent->colour;
	      x->parent->colour = BLACK;
	      w->left->colour = BLACK;
	      RightRotate (tree, x->parent);
	      x = root;		/* this is to exit while loop */
	    }
	}
    }
  x->colour = BLACK;

}

static void
balance (Node * n, TreeMap * t)
{
  Node *y;
  Node *x = n;
  while (x->parent->colour == RED)
    {				/* use sentinel colour as exit loop */
      if (x->parent == x->parent->parent->left)
	{
	  y = x->parent->parent->right;	/* y 'uncle' of x */
	  if (y->colour == RED)
	    {
	      x->parent->colour = BLACK;
	      y->colour = BLACK;
	      x->parent->parent->colour = RED;
	      x = x->parent->parent;
	    }
	  else
	    {
	      if (x == x->parent->right)
		{
		  x = x->parent;
		  LeftRotate (t, x);
		}
	      x->parent->colour = BLACK;
	      x->parent->parent->colour = RED;
	      RightRotate (t, x->parent->parent);
	    }
	}
      else
	{			/* case for x->parent == x->parent->parent->right */
	  y = x->parent->parent->left;
	  if (y->colour == RED)
	    {
	      x->parent->colour = BLACK;
	      y->colour = BLACK;
	      x->parent->parent->colour = RED;
	      x = x->parent->parent;
	    }
	  else
	    {
	      if (x == x->parent->left)
		{
		  x = x->parent;
		  RightRotate (t, x);
		}
	      x->parent->colour = BLACK;
	      x->parent->parent->colour = RED;
	      LeftRotate (t, x->parent->parent);
	    }
	}
    }
  t->rootNode->left->colour = BLACK;
}

static Node *
TreeSuccessor (TreeMap * t, Node * x)
{
  Node *y;
  Node *root = t->rootNode;

  if ((y = x->right) != t->nil)
    {
      while (y->left != t->nil)
	{
	  y = y->left;
	}
      return (y);
    }
  else
    {
      y = x->parent;
      while (x == y->right)
	{
	  x = y;
	  y = y->parent;
	}
      if (y == root)
	return (t->nil);
      return (y);
    }
}

static void
removeNode (TreeMap * t, Node * p)
{
  Node *y;
  Node *x;
  Node *root = t->rootNode;


  y = ((p->left == t->nil)
       || (p->right == t->nil)) ? p : TreeSuccessor (t, p);
  x = ((y->left == t->nil)) ? y->right : y->left;

  x->parent = y->parent;
  if (root == x->parent)
    {

      root->left = x;
    }
  else
    {
      if (y == y->parent->left)
	{
	  y->parent->left = x;
	}
      else
	{
	  y->parent->right = x;
	}
    }
  if (y != p)
    {

      if (!(y->colour == RED))
	balanceAfterDeletion (t, x);

      t->delete (&p);
      y->left = p->left;
      y->right = p->right;
      y->parent = p->parent;
      y->colour = p->colour;
      p->left->parent = p->right->parent = y;
      if (p == p->parent->left)
	{
	  p->parent->left = y;
	}
      else
	{
	  p->parent->right = y;
	}
      free (p);
    }
  else
    {
      t->delete (&p);
      if (!(p->colour == RED))
	balanceAfterDeletion (t, x);
      free (p);
    }
}

/* public stuff...*/
Node *
searchKey (TreeMap * t, void *key)
{
  Node *current;
  Node *found;
  assert (t != NULL);
  current = t->rootNode->left;
  pthread_mutex_lock (&t->big_lock);

  if (t->rootNode->left == t->nil)
    {

      pthread_mutex_unlock (&t->big_lock);
      return t->nil;
    }
  found = search (t, current, key);
  pthread_mutex_unlock (&t->big_lock);
  if (found == t->nil)
    return t->nil;
  else
    return found;
}

void
destroyKey (TreeMap * t, void *key)
{
  Node *k = searchKey (t, key);
  pthread_mutex_lock (&t->big_lock);
  removeNode (t, k);
  pthread_mutex_unlock (&t->big_lock);
}

void
insertKey (TreeMap * t, void *key, void *value)
{
  Node *n;
  pthread_mutex_lock (&t->big_lock);
  n = t->clone (key, value);
  n->colour = RED;
  insertNode (t, n);
  balance (n, t);
  pthread_mutex_unlock (&t->big_lock);
}


void
replaceKey (TreeMap * t, void *key, void *newKey)
{

  Node *current;
  Node *cloned;
  current = searchKey (t, key);
  if (current == t->nil)
    {
      printf ("node not found for replacekey\n");
      return;
    }
  pthread_mutex_lock (&t->big_lock);
  cloned = t->clone (newKey, current->value);
  cloned->colour = RED;
  removeNode (t, current);
  insertNode (t, cloned);
  balance (cloned, t);
  pthread_mutex_unlock (&t->big_lock);

}

Node *
updateKey (TreeMap * t, void *key, void *value)
{
  Node *current;
  Node *n;
  current = searchKey (t, key);
  if (current == t->nil)
    return t->nil;
  pthread_mutex_lock (&t->big_lock);
  n = t->clone (key, value);
  n->colour = current->colour;
  n->left = current->left;
  n->right = current->right;
  if (n->left != t->nil)
    n->left->parent = n;
  if (n->right != t->nil)
    n->right->parent = n;
  n->parent = current->parent;
  if (current->parent->left == current)
    current->parent->left = n;
  else
    current->parent->right = n;

  t->delete (&current);
  free (current);
  pthread_mutex_unlock (&t->big_lock);
  return n;
}

Node *
eraseKey (TreeMap * t, void *key)
{
  Node *ret;
  Node *k = searchKey (t, key);
  if (k == t->nil)
    return t->nil;
  pthread_mutex_lock (&t->big_lock);
  ret = t->clone (k->key, k->value);
  removeNode (t, k);
  pthread_mutex_unlock (&t->big_lock);
  return ret;
}

int
eraseNode (TreeMap * t, Node * n)
{
  if (n == t->nil)
    return 0;

  t->delete (&n);
  return 1;
}

void
destroy_tree_map (TreeMap * t)
{
  if (t == NULL)
    return;
  pthread_mutex_destroy (&t->big_lock);
  freeTree (t, t->rootNode->left);
  free (t->rootNode);
  free (t->nil);
  free (t);
}
