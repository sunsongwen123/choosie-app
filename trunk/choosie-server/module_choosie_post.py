from google.appengine.ext import db
from google.appengine.api import images
from module_user import User
from module_vote import Vote
from module_comment import Comment
from utils import Utils
import logging

class ChoosiePost(db.Model):
  photo1 = db.BlobProperty(required=True)
  photo2 = db.BlobProperty(required=True)
  question = db.StringProperty(required=True)
  created_at = db.DateTimeProperty(auto_now_add = True)
  user = db.ReferenceProperty(User,required=True)

  def to_json(self):
    votes = self.votes()
    return {"key": str(self.key()),
            "user": self.user.to_short_json(),
            "votes": Utils.items_to_json(votes),
            "comments": Utils.items_to_json(self.comments()),
            "photo1": self.photo_path(1),
            "photo2": self.photo_path(2),
            "votes1": self.votes_for_count(votes, 1),
            "votes2": self.votes_for_count(votes, 2),
            "question": str(self.question),
            "created_at": str(self.created_at)
           }

  def votes1(self):
    logging.info(str(self.votes_for_count(self.votes() ,1)))
    return self.votes_for_count(self.votes() ,1)

  def votes2(self):
    return self.votes_for_count(self.votes(), 2)

  def votes(self):
    return Vote.all().ancestor(self)

  def comments(self):
    return Comment.all().ancestor(self)

  def votes_for_count(self, votes, vote_for):
    count = 0
    for vote in votes:
      if vote.vote_for == vote_for:
        count += 1
    return count

  def photo_path(self, which_photo):
    return '/photo?which_photo=%s&post_key=%s' % (which_photo, self.key()) 