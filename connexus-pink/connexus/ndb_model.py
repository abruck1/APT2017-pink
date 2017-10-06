from google.appengine.ext import ndb
from google.appengine.datastore.datastore_query import Cursor

STREAM_IMAGES_PER_PAGE = 3

class Stream(ndb.Model):
    owner = ndb.StringProperty()
    name = ndb.StringProperty()
    createDate = ndb.DateTimeProperty(auto_now_add=True)
    coverImageURL = ndb.StringProperty()
    viewCount = ndb.IntegerProperty()
    imageCount = ndb.IntegerProperty()
    lastPicDate = ndb.DateTimeProperty()
    tags = ndb.StringProperty(repeated=True)
    imageCount = ndb.IntegerProperty()
    viewsInPastHour = ndb.IntegerProperty()


class StreamSubscriber(ndb.Model):
    stream = ndb.KeyProperty(kind='Stream')
    user = ndb.StringProperty()


# uses parent=stream.key on creation
class StreamImage(ndb.Model):
    imageBlobKey = ndb.BlobKeyProperty()
    createDate = ndb.DateTimeProperty(auto_now_add=True)

    @classmethod
    def cursor_pagination(cls, ancestor_key, prev_cursor_str, next_cursor_str):
        if not prev_cursor_str and not next_cursor_str:
            print("option 1")
            objects, next_cursor, more = cls.query(ancestor=ancestor_key).order(-cls.createDate).fetch_page(STREAM_IMAGES_PER_PAGE)
            prev_cursor_str = ''
            if next_cursor:
                next_cursor_str = next_cursor.urlsafe()
            else:
                next_cursor_str = ''
            next_ = True if more else False
            prev = False
        elif next_cursor_str:
            print("option 2")

            cursor = Cursor(urlsafe=next_cursor_str)
            objects, next_cursor, more = cls.query(ancestor=ancestor_key).order(-cls.createDate).fetch_page(STREAM_IMAGES_PER_PAGE, start_cursor=cursor)
            prev_cursor_str = next_cursor_str
            next_cursor_str = next_cursor.urlsafe()
            prev = True
            next_ = True if more else False
        elif prev_cursor_str:
            print("option 3")

            cursor = Cursor(urlsafe=prev_cursor_str)
            objects, next_cursor, more = cls.query(ancestor=ancestor_key).order(-cls.createDate).fetch_page(STREAM_IMAGES_PER_PAGE, start_cursor=cursor)
            objects.reverse()
            next_cursor_str = prev_cursor_str
            prev_cursor_str = next_cursor.urlsafe()
            prev = True if more else False
            next_ = True

        return {'objects': objects, 'next_cursor': next_cursor_str, 'prev_cursor': prev_cursor_str, 'prev': prev,
                'next': next_}


class StreamView(ndb.Model):
    viewDate = ndb.DateTimeProperty(auto_now_add=True)


class AppConfig(ndb.Model):
    trendEmailSend = ndb.StringProperty()  # options are: never, minutes, hour, day
