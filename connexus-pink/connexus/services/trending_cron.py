import webapp2
import datetime

from connexus.ndb_model import *
from connexus.common import *
from time import time


class TrendingCron(webapp2.RequestHandler):

    def get(self):

        # **************************update past hour views for each stream********************
        # load stream view items and delete all items with timestamp older than 1 hour
        streamviews = StreamView.query().order(StreamView.viewDate)
        if streamviews is None:
            print("streamviews is empty")
        else:
            for streamview in streamviews:
                print("{}, {}".format(streamview.viewDate, datetime.datetime.now() - datetime.timedelta(hours=1)))
                if streamview.viewDate < datetime.datetime.now() - datetime.timedelta(hours=1):
                    streamview.key.delete()
                    print("streamview deleted")

        # get id from stream view parent for each item in stream view
        # loop through results and store count for each id in a dictionary with id as key and count as value
        trending_dict = {}
        for streamview in streamviews:
            parent = streamview.key.parent().get()
            parent_id = parent.key.id()
            if parent_id in trending_dict:
                trending_dict[parent_id] += 1
            else:
                trending_dict[parent_id] = 1

        # update stream table for counts in the past hour
        for key, count in trending_dict.iteritems():
            Stream.get_by_id(int(key)).viewsInPastHour = count
        # ************************************************************************************

        # **************************check email report setting and send email if necessary****
        reportEmail = AppConfig.query().fetch(1)[0]
        send_reports_to = [
            "wey.matt@utexas.edu",
            "ee382vta @ gmail.com"
        ]
        top_three_streams = Stream.query().order(-Stream.viewsInPastHour).fetch(3)
        if reportEmail == 1:
            for recipient in send_reports_to:
                send_trend_report(recipient, top_three_streams)
        if reportEmail == 2:
            timestamp = time()
            if 0 <= timestamp % 3600 < 300:
                for recipient in send_reports_to:
                    send_trend_report(recipient, top_three_streams)
        if reportEmail == 3:
            if datetime.time(hour=0, minute=0) <= datetime.now() < datetime.time(hour=0, minute=5):
                for recipient in send_reports_to:
                    send_trend_report(recipient, top_three_streams)
        self.response.status = 204
