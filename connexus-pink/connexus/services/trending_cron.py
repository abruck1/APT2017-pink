import datetime
import webapp2
from connexus.common import *
from connexus.ndb_model import *
from time import time


class TrendingCron(webapp2.RequestHandler):
    def get(self):
        # ************************** update past hour views for each stream ********************
        # load stream view items and delete all items with timestamp older than 1 hour
        streamviews = StreamView.query().order(StreamView.viewDate)
        if streamviews is None:
            pass
        else:
            for streamview in streamviews:
                print("{}, {}".format(streamview.viewDate, datetime.datetime.now() - datetime.timedelta(hours=1)))
                if streamview.viewDate < datetime.datetime.now() - datetime.timedelta(hours=1):
                    streamview.key.delete()

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
            stream = Stream.get_by_id(int(key))
            stream.viewsInPastHour = count
            stream.put()
        # ************************************************************************************

        # ************************** check email report setting and send email if necessary ****
        report_email = AppConfig.query().fetch(1)
        if report_email:
            report_email = report_email[0].trendEmailSend
            # todo: update email addresses after finalized to send emails to instructors
            # send_reports_to = [
            #     "wey.matt@utexas.edu",
            #     "ee382vta@gmail.com"
            # ]
            send_reports_to = [
                "wey.matt@utexas.edu"
            ]
            top_three_streams = Stream.query().order(-Stream.viewsInPastHour).fetch(3)
            top_three_stream_ids = []
            if top_three_streams:
                for stream in top_three_streams:
                    top_three_stream_ids.append(stream.key.id())
            if report_email == 'minutes':
                for recipient in send_reports_to:
                    send_trend_report(recipient, top_three_stream_ids)
            if report_email == 'hour':
                timestamp = time()
                if 0 <= timestamp % 3600 < 300:
                    for recipient in send_reports_to:
                        send_trend_report(recipient, top_three_stream_ids)
            if report_email == 'day':
                timestamp = time()
                if 18000 <= timestamp % 86400 < 18300:
                    for recipient in send_reports_to:
                        send_trend_report(recipient, top_three_stream_ids)

        self.response.status = 204
